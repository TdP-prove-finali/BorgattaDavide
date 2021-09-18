package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.tesi.db.GiocatoriDao;

public class Model {
	
	private GiocatoriDao dao;
	private SimpleWeightedGraph<Vertice, DefaultWeightedEdge> grafo;
	private Map<Integer,Ruolo> idMapRuoli;
	private int budgetRimasto;
	private List<Giocatore> listaGiocatori;
	
	// variabili per la squadra migliore
	private List<Vertice> squadraBest;
	private double overallBest; 
	private int intesaBest;
	private int costoBest;
	
	public Model() {
		dao = new GiocatoriDao();
	}
	
	public String verificaParametri(String modulo, int budget, List<Giocatore>  giocatori) {
		String risultato = "" ; 
		budgetRimasto = 0;
		
		// creo la mia IDMap per i ruoli 
		this.idMapRuoli = new HashMap<>();
		for(Ruolo r : dao.getRuoli()) {
			this.idMapRuoli.put(r.getId(), r);
		}
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// ricavo i vertici e gli archi utilizzando il modulo
		List<Collegamenti> archi = dao.getCollegamenti(modulo);
		
		// controllo se il vertice è gia presente, se non è presente lo inserisco
		// controllo se il collegamento è presente, se non lo è inserisco l'arco
		for(Collegamenti col : archi) {
			Vertice v1= new Vertice(idMapRuoli.get(col.getGiocatore1()), null);
			Vertice v2= new Vertice(idMapRuoli.get(col.getGiocatore2()), null);
			
			if(!this.grafo.containsVertex(v1)) {
				this.grafo.addVertex(v1);
			}
			if(!this.grafo.containsVertex(v2)) {
				this.grafo.addVertex(v2);
			}
			if(!this.grafo.containsEdge(v1,v2)) {
				// inizialmente metto un peso 2 che è il massimo possibile (utile per verifiche preliminari) 
				Graphs.addEdge(this.grafo, v1, v2, 2);
			}
		}
		
		// scorro i giocatori e cerco di inserirli nel grafo
		int inserito = 0; 
		for(Giocatore g : giocatori) {
			for(Vertice v : this.grafo.vertexSet()) {
				// controllo se il giocatore è gia presente nel grafo
				if(! (v.getGiocatore() == null) ) {
						if(v.getGiocatore().equals(g)) {
							risultato = "ERRORE: \nHai selezionato due volte lo stesso giocatore";
							return risultato;
						}
				}
				// inserisco il giocatore scorrendo gli archi del grafo e andando a contare se 
				// è possibile inserire nel grafo tutti i giocatori (rispetto vincoli di modulo)
				if((g.getPosizione()).equals(v.getRuolo().getAbbreviazione()) && v.getGiocatore() == null) {
					
					Integer idVertice = v.getRuolo().getId();
					for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
						if (this.grafo.getEdgeSource(e).getRuolo().getId()==idVertice) {
							this.grafo.getEdgeSource(e).setGiocatore(g);
						}
						if(this.grafo.getEdgeTarget(e).getRuolo().getId()==idVertice) {
							this.grafo.getEdgeTarget(e).setGiocatore(g);
						}
					}
					inserito++; 
					break; //  il ciclo for viene interrotto evitando che un giocatore venga inserito due volte
				}
			}
		}
		// controllo se tutti i giocatori sono stati inseriti 
		if (inserito != 4) {
			risultato = "ATTENZIONE: \nI giocatori selezionati non sono adatti al modulo \nOppure  "
					+ "hai selezionato due giocatori nello stesso ruolo";
			return risultato;
		}
		
		// controllo preliminare sull'intesa 
		
		this.aggiungoPesoGrafo();
				
		if(this.getIntesaSquadra() == -1) {
			risultato = "ATTENZIONE: \nCon i giocatori selezionati non è possibile avere una squadra con un minimo"
					+ " di intesa individuale pari a 7 ";
			return risultato;
							
		}
		
		// controllo del prezzo
		if(budget <= this.getPrezzo()) {
			risultato = "Attenzione: \nI giocatori selezioni hanno un prezzo piu alto del budget messo a disposizione";
			return risultato;
		}
		else {
			budgetRimasto = budget - this.getPrezzo();
			risultato = "Giusto" ;
		}
		/* //verifica per vedere i vertici creati 
		for (Vertice v : this.grafo.vertexSet()) {
			System.out.println(v.getRuolo().getId() + " " + v.getGiocatore());
	
		}
		*/
		return risultato; 
	}

	
	
	public List<Vertice> getSquadra(){
		
		//inizializzo lista dove andrò ad aggiungere i giocatori
		//inizializzo set per i ruoli e giocatori parziali
		List<Ruolo> ruoloParziale = new ArrayList<Ruolo>();
		Set<Vertice> giocatoriParziale = new HashSet<Vertice>();
		Set<Giocatore> listaGiocatori = new HashSet<Giocatore>();
		// aggiungo gli elementi ai set e alla lista appena creati
		for (Vertice v : this.grafo.vertexSet()) {
			
			if (!(v.getGiocatore() == null)) {
				giocatoriParziale.add(v);
				listaGiocatori.add(v.getGiocatore());
			}else {
				ruoloParziale.add(v.getRuolo());
			}
		}
		
		
		// inizializzo le variabili per la ricorsione 
		this.intesaBest = 0;
		this.overallBest = 0.0; 
		this.costoBest = 0; 
		this.squadraBest = new ArrayList<>();
		
		this.ricorsione(ruoloParziale, giocatoriParziale,listaGiocatori, this.budgetRimasto);
		
		/*
	    // calcolo il tempo della ricorsione 
		double start = System.nanoTime();
		this.ricorsione(ruoloParziale, giocatoriParziale,listaGiocatori, this.budgetRimasto);
		double stop = System.nanoTime();
		
		System.out.println("Squadra trovata in Secondi: " + (stop - start)/1e9);
		*/
		
		// ordino la squadra in base al ruolo 
		Collections.sort(squadraBest);
	
		return this.squadraBest;
	}
	
	// metodo ricorsivo
	private void ricorsione(List<Ruolo> ruoloParziale,Set<Vertice> giocatoriParziale,Set<Giocatore> listaGiocatori, Integer budget) {
		
		
		// caso finale non ho più ruoli 
		if(ruoloParziale.size() == 0 ) {
			// se arrivato a questo punto allora la formazione è possibile come budget 
			
			// evito di fare getOverall 3 volte successivamente 
			double overall = this.getOverall(listaGiocatori);
			
			if(this.intesaBest == 100) {
				if (overall < this.overallBest) {
					// se overallBest = 100, inutile continuare se la squadra ha overall < di overallBest
					// evito di caricare il grafo inultimente e quindi risparmio tempo 
					return;
				}
			}
			
			// carico il grafo e aggiungo il peso 
			this.caricaGrafo(giocatoriParziale);
			this.aggiungoPesoGrafo();
			// evito di fare getIntesaSquadra 3 volte successivamente
			int intesa = this.getIntesaSquadra();
			int prezzo = getPrezzo();
			if( (intesa > 0 ) && ( (intesa +  overall) > (this.intesaBest + this.overallBest) ) ) {
				// trovato una squadra con valore (intesa+overall) maggiore a quella della squadra best
				//aggiorno le variabili della squadra best 
				this.costoBest = prezzo;
				this.intesaBest = intesa;
				this.overallBest = overall;
				
				/*// verifica output delle squadre trovate
				System.out.println("\nTrovata squadra, costo = " + this.costoBest + " intesa = "
				                                        + this.intesaBest + " overall = " + this.overallBest);
				for(Vertice g : this.grafo.vertexSet()) {
					System.out.println(g.getGiocatore());
				} 
				*/
				//aggiorno variabile squadra best 
				this.squadraBest.clear();
				for(Vertice g : giocatoriParziale) {
					squadraBest.add(g);
				} 
				
				// controllo se il valore (intesa+overall) è uguale a quella della squadra best 
			} else if ((intesa > 0 ) && ( (intesa +  overall) ==  (this.intesaBest + this.overallBest) ) ){
				//controllo in questo caso se il prezzo è più basso
				if(prezzo < this.costoBest) {
					// squadra trovata è più economica aggiorno le variabili 
					this.costoBest = prezzo;
					this.intesaBest = intesa;
					this.overallBest = overall;
					
					/*// verifica output delle squadre trovate
					System.out.println("\nTrovata squadra, costo = " + this.costoBest + " intesa = "
					                                         + this.intesaBest + " overall = " + this.overallBest);
					for(Vertice g : this.grafo.vertexSet()) {
						System.out.println(g.getGiocatore());
					} 
					*/
					//aggiorno variabile squadra best
					this.squadraBest.clear();
					for(Vertice g : giocatoriParziale) {
						squadraBest.add(g);
					} 
				}
			}
				
		}
		// ci sono ancori ruoli, il metodo ricorsivo continua
		else {
		Ruolo r = ruoloParziale.get(0);
			for(Giocatore g : this.listaGiocatori) {
				// controllo che il giocatore non sia già inserito nella squadra 
				if(!(listaGiocatori.contains(g))) {
					// controllo del prezzo giocatore 
					if( (budget - g.getPrezzo()) >= 0 ) {
						// controllo se c'è un vertice libero che rispetta i requisiti di posizione
						if((r.getAbbreviazione().equals(g.getPosizione()))) {
						 
					    // rimuovo il ruolo dalla lista 
						// inserisco giocatore, aggiorno il budget, eseguo nuova ricorsione
						List<Ruolo> ruoliRimasti = new ArrayList<Ruolo>(ruoloParziale);
						ruoliRimasti.remove(r);
						Vertice nuovoGiocatore = new Vertice(r,g);
						giocatoriParziale.add(nuovoGiocatore);
						listaGiocatori.add(g);
						Integer budgetR = budget - g.getPrezzo();
						
						// lancio ricorsione
						this.ricorsione(ruoliRimasti, giocatoriParziale,listaGiocatori, budgetR);
						
						// backtracking rimuovo il giocatore appena aggiunto
						
						giocatoriParziale.remove(nuovoGiocatore);
						listaGiocatori.remove(g);
						}
					}
				}
			}
		}
	}
		
	
	// funzione che calcola il prezzo della squadra 
	public int getPrezzo() {
		int prezzo = 0; 
		for(Vertice v : this.grafo.vertexSet()) {
			if(!(v.getGiocatore() == null)) {
				prezzo += v.getGiocatore().getPrezzo();
			}
		}
		return prezzo; 
	}
	
	// funzione per caricare i pesi 
	public void aggiungoPesoGrafo() {
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Giocatore g1 = this.grafo.getEdgeSource(e).getGiocatore();
			Giocatore g2 = this.grafo.getEdgeTarget(e).getGiocatore();
			
			if ( (g1 != null) && (g2 != null)) {
				// aggiorno il peso
				this.grafo.setEdgeWeight(e,this.getPeso(g1, g2));
			}		
		}		
	}
	
	// funzione che calcola il peso dell'arco 
	public int getPeso(Giocatore g1, Giocatore g2) {
		// tripla intesa, i tre attributi devono coincidere (stessa squadra implica stessa lega)
		if( (g1.getNazionalita().equals(g2.getNazionalita())) && (g1.getSquadra().equals(g2.getSquadra()))) {
			return 2;
		}// doppia intesa, 2 attributi in comune
		else if ((g1.getSquadra().equals(g2.getSquadra())) || 
				(g1.getLega().equals(g2.getLega()) && g1.getNazionalita().equals(g2.getNazionalita()))) {
			return 1;
		}// singola intesa, un attributo in comune 
		else if ((g1.getLega().equals(g2.getLega())) || g1.getNazionalita().equals(g2.getNazionalita())) {
			return 0;
		}// nessun attributo in comune 
		else 
			return -1;
	}
	
	public int getIntesaSquadra() {
		int intesaTot = 0;
		for (Vertice v : this.grafo.vertexSet()) {
			int intesa = getIntesaGiocatore(v);
			// controllo che venga rispettato il vincolo di almeno 7 per l'intesa di ogni singolo giocatore
			if(intesa < 7) {
				return -1;
			} else {
				intesaTot += intesa;
			}
		}//il massimo di intesa per una squadra puo essere 100
		if (intesaTot > 100) {
			intesaTot = 100;
		}
		return intesaTot;
	}
	
	public int getIntesaGiocatore(Vertice v) {
		int pesoFinale = 0;
		int grado = this.grafo.degreeOf(v);
		for (Vertice adiacente : Graphs.neighborListOf(this.grafo, v)) {
			int pesoArco = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(v, adiacente));
			pesoFinale += pesoArco;
			}
		if (pesoFinale >= 0) {
			return 10;
		} else if (pesoFinale == -1 || (pesoFinale == -2 && grado > 2) || (pesoFinale == -3 && grado > 4)) {
			return 7;
		} else {
			return 4;
		}
	}
	
	public double getOverall(Set<Giocatore> giocatori) {
		double somma = 0.0;
		
		for(Giocatore g: giocatori) {
			somma+= g.getOverall();
		}
		
		double risultato = somma/11;
		
		return risultato;
		
	}

	public void caricaGrafo(Set<Vertice> giocatori) {
		//inserisco i giocatori di parziale 
		for(Vertice g : giocatori) {
			Integer idVertice = g.getRuolo().getId();
			for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
				if (this.grafo.getEdgeSource(e).getRuolo().getId()==idVertice) {
					this.grafo.getEdgeSource(e).setGiocatore(g.getGiocatore());
				}
				if(this.grafo.getEdgeTarget(e).getRuolo().getId()==idVertice) {
					this.grafo.getEdgeTarget(e).setGiocatore(g.getGiocatore());
				}
			}
		}	
	}
	
	// funzione per restituire budget rimasto
	public int getBudgetRimasto() {
		return this.budgetRimasto;
	}
	
	// funzione per restituire overall
	public int getOverallFinale() {
		int risultato = Math.toIntExact(Math.round(this.overallBest));
		return risultato; 
	}
	
	//funzione per restituire intesa
	public int getIntesaFinale() {
		return this.intesaBest;
	}
	
	//funzione per restituire prezzo
	public int getCostoFinale() {
		return this.costoBest;
	}
	
	public List<String> getModuli(){
		return dao.getModuli();
	}
	
	// funzioni per settare la tendina nel controller e inizializzare listaGiocatori
	public List<Giocatore> getGiocatoriTop(){
		this.listaGiocatori = new ArrayList<>(dao.getGiocatoriTop());
		return this.listaGiocatori;
	}
	public List<Giocatore> getGiocatoriAlto(){
		this.listaGiocatori = new ArrayList<>(dao.getGiocatoriAlto());
		return this.listaGiocatori;
	}
	public List<Giocatore> getGiocatoriMedio(){
		this.listaGiocatori = new ArrayList<>(dao.getGiocatoriMedio());
		return this.listaGiocatori;
	}
	public List<Giocatore> getGiocatoriBasso(){
		this.listaGiocatori = new ArrayList<>(dao.getGiocatoriBasso());
		return this.listaGiocatori;
	}
	public List<Giocatore> getGiocatoriBase(){
		this.listaGiocatori = new ArrayList<>(dao.getGiocatoriBase());
		return this.listaGiocatori;
	}
	
}

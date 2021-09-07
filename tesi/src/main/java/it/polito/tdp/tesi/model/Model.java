package it.polito.tdp.tesi.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.tesi.db.GiocatoriDao;

public class Model {
	
	private GiocatoriDao dao;
	private SimpleWeightedGraph<Vertice, DefaultWeightedEdge> grafo;
	private Map<Integer,Ruolo> idMapRuoli;
	
	public Model() {
		dao = new GiocatoriDao();
	}
	
	public List<String> getModuli(){
		return dao.getModuli();
	}
	
	public List<Giocatore> getGiocatori(){
		return dao.getGiocatori();
	}
	
	public String verificaParametri(String modulo, int budget, List<Giocatore>  giocatori) {
		String risultato = "" ; 
		
		this.idMapRuoli = new HashMap<>();
		for(Ruolo r : dao.getRuoli()) {
			this.idMapRuoli.put(r.getId(), r);
		}
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// ricavo i vertici e gli archi utilizzando il modulo
		List<Collegamenti> archi = dao.getCollegamenti(modulo);
		
		// controllo se il vertice è gia presente, se non è presente inserisco
		// controllo se il collegamento è presente, se non lo è inserisco arco
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
							risultato = "ERRORE! Hai selezionato due volte lo stesso giocatore";
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
					break; 
				}
			}
		}
		// controllo se tutti i giocatori sono stati inseriti 
		if (inserito != 4) {
			risultato += "Attenzione, i giocatori selezionati non sono adatti al modulo o "
					+ "hai selezionato due giocatori nello stesso ruolo";
			return risultato;
		}
		
		// controllo preliminare sull'intesa 
		
		this.aggiungoPeso();
				
		if(this.getIntesaSquadra() == -1) {
			risultato = "Attenzione, con i giocatori selezionati non è possibile avere una squadra con un minimo"
					+ " di intesa pari a 7 ";
			return risultato;
							
		}
		
		// controllo del prezzo
		if(budget <= this.getPrezzo()) {
			risultato = "Attenzione, i giocatori selezioni hanno un prezzo piu alto del budget messo a disposizione";
			return risultato;
		}
		else {
			int rimanente = budget - this.getPrezzo();
			risultato += "Parametri inseriti in modo corretto. "
					+ "\nI giocatori selezionati rispettano i vincoli di intesa e prezzo, Budget ancora a disposizione "
					+ "per i restanti componenti della squadra: " + rimanente ;
		}
		
		for (Vertice v : this.grafo.vertexSet()) {
			System.out.println(v.getRuolo().getId() + " " + v.getGiocatore());
	
		}
		
		return risultato; 
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
	public void aggiungoPeso() {
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
		}
		// doppia intesa, 2 attributi in comune
		else if ((g1.getSquadra().equals(g2.getSquadra())) || 
				(g1.getLega().equals(g2.getLega()) && g1.getNazionalita().equals(g2.getNazionalita()))) {
			return 1;
		}
		// singola intesa, un attributo in comune 
		else if ((g1.getLega().equals(g2.getLega())) || g1.getNazionalita().equals(g2.getNazionalita())) {
			return 0;
		}
		// nessun attributo in comune 
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
			}
			else {
				intesaTot += intesa;
			}
		}

		//il massimo di intesa per una squadra puo essere 100
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
	

}

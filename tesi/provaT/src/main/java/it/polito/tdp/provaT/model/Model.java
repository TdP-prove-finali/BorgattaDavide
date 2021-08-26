package it.polito.tdp.provaT.model;

import java.util.List;

public class Model {
	
	// funzione crea grafo
	
	private Integer budget = 0;  
	
	public List<Giocatore> creaGrafo(String modulo,Integer budget, String qualita, Giocatore giocatore1, Giocatore giocatore2, 
			Giocatore giocatore3, Giocatore giocatore4){
		
		
		
		return null;
	}
	
	// funzione calcola prezzo 
	
	public Integer calcolaPrezzo(List<Giocatore> squadra) {
		
		Integer prezzoTotale = 0;
		
		for (Giocatore g : squadra) {
			prezzoTotale += g.getPrezzo();
		}
		
		return prezzoTotale; 
	}

}

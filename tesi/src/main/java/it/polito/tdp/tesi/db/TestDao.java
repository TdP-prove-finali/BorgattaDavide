package it.polito.tdp.tesi.db;

import it.polito.tdp.tesi.model.Collegamenti;
import it.polito.tdp.tesi.model.Ruolo;

public class TestDao {

	public static void main(String[] args) {
		GiocatoriDao dao = new GiocatoriDao();
		
		System.out.println("Lista Ruoli:");
		for(Ruolo r : dao.getRuoli())
			System.out.println(r);
		
		System.out.println("\n\n Lista Affinita 4-4-2:");
		for(Collegamenti col : dao.getCollegamenti("4-4-2"))
			System.out.println(col);
			
		
	}

}

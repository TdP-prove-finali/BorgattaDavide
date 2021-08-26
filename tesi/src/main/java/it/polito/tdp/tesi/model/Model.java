package it.polito.tdp.tesi.model;

import java.util.List;

import it.polito.tdp.tesi.db.GiocatoriDao;

public class Model {
	
	private GiocatoriDao dao;
	
	public Model() {
		dao = new GiocatoriDao();
	}
	
	public List<String> getModuli(){
		return dao.getModuli();
	}
	
	public List<Giocatore> getGiocatori(){
		return dao.getGiocatori();
	}

}

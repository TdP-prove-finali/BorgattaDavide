package it.polito.tdp.tesi.model;

public class Collegamenti {
	
	private String modulo;
	private Integer giocatore1; 
	private Integer giocatore2;
	
	public Collegamenti(String modulo, Integer giocatore1, Integer giocatore2) {
		super();
		this.modulo = modulo;
		this.giocatore1 = giocatore1;
		this.giocatore2 = giocatore2;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public Integer getGiocatore1() {
		return giocatore1;
	}

	public void setGiocatore1(Integer giocatore1) {
		this.giocatore1 = giocatore1;
	}

	public Integer getGiocatore2() {
		return giocatore2;
	}

	public void setGiocatore2(Integer giocatore2) {
		this.giocatore2 = giocatore2;
	}

	@Override
	public String toString() {
		return "Collegamento: " + giocatore1 + " e " + giocatore2 ;
	}

	
}

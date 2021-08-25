package it.polito.tdp.tesi.model;

public class Ruolo {
	
	private Integer id;
	private String abbreviazione;
	private String nome;
	
	public Ruolo(Integer id, String abbreviazione, String nome) {
		super();
		this.id = id;
		this.abbreviazione = abbreviazione;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAbbreviazione() {
		return abbreviazione;
	}

	public void setAbbreviazione(String abbreviazione) {
		this.abbreviazione = abbreviazione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Ruolo " + nome + " (" + abbreviazione + ") ";
	} 
	
	

}

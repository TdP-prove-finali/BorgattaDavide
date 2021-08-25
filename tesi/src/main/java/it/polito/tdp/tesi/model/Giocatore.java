package it.polito.tdp.tesi.model;

public class Giocatore {
	
	private Integer id;
	private String nome;
	private String rarita;
	private Integer overall;
	private String squadra; 
	private String lega;
	private String nazionalita;
	private String posizione; 
	private Integer prezzo;
	
	public Giocatore(Integer id, String nome, String rarita, Integer overall, String squadra, String lega,
			String nazionalita, String posizione, Integer prezzo) {
		super();
		this.id = id;
		this.nome = nome;
		this.rarita = rarita;
		this.overall = overall;
		this.squadra = squadra;
		this.lega = lega;
		this.nazionalita = nazionalita;
		this.posizione = posizione;
		this.prezzo = prezzo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRarita() {
		return rarita;
	}

	public void setRarita(String rarita) {
		this.rarita = rarita;
	}

	public Integer getOverall() {
		return overall;
	}

	public void setOverall(Integer overall) {
		this.overall = overall;
	}

	public String getSquadra() {
		return squadra;
	}

	public void setSquadra(String squadra) {
		this.squadra = squadra;
	}

	public String getLega() {
		return lega;
	}

	public void setLega(String lega) {
		this.lega = lega;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public String getPosizione() {
		return posizione;
	}

	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}

	public Integer getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Integer prezzo) {
		this.prezzo = prezzo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Giocatore other = (Giocatore) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  nome + " " + overall + "( " + posizione + " )";
	}

}

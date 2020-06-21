package it.polito.tdp.model;

import java.time.LocalDateTime;

public class Agente {
	
	private Integer id; 
	private boolean libero;
	private Distretto posizione;
	private LocalDateTime oraPartenzaDalDistretto;
	private LocalDateTime oraArrrivoAlDistretto;
	
	public Agente(Integer id, boolean libero, Distretto posizione) {
		this.id = id;
		this.libero = libero;
		this.posizione = posizione;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isLibero() {
		return libero;
	}

	public void setLibero(boolean libero) {
		this.libero = libero;
	}

	public Distretto getPosizione() {
		return posizione;
	}

	public void setPosizione(Distretto posizione) {
		this.posizione = posizione;
	}

	public LocalDateTime getOraPartenzaDalDistretto() {
		return oraPartenzaDalDistretto;
	}

	public void setOraPartenzaDalDistretto(LocalDateTime oraPartenzaDalDistretto) {
		this.oraPartenzaDalDistretto = oraPartenzaDalDistretto;
	}

	public LocalDateTime getOraArrrivoAlDistretto() {
		return oraArrrivoAlDistretto;
	}

	public void setOraArrrivoAlDistretto(LocalDateTime oraArrrivoAlDistretto) {
		this.oraArrrivoAlDistretto = oraArrrivoAlDistretto;
	}
	
	

}

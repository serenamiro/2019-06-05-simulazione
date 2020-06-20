package it.polito.tdp.model;

public class DistrettoVicino implements Comparable<DistrettoVicino>{
	
	private Distretto d;
	private double distanza;
	
	public DistrettoVicino(Distretto d, double distanza) {
		this.d = d;
		this.distanza = distanza;
	}

	public Distretto getD() {
		return d;
	}

	public void setD(Distretto d) {
		this.d = d;
	}

	public double getDistanza() {
		return distanza;
	}

	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}

	@Override
	public int compareTo(DistrettoVicino o) {
		// TODO Auto-generated method stub
		return (int)(this.distanza-o.distanza);
	}

	@Override
	public String toString() {
		return "Distretto: " + d.getId() + ", distanza " + distanza;
	}

}

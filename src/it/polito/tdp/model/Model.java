package it.polito.tdp.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	public Graph<Distretto, DefaultWeightedEdge> grafo;
	private Map<Integer, Distretto> mappaDistretti;

	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getAnni(){
		return dao.getYears();
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleWeightedGraph<Distretto, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.mappaDistretti = dao.getDistrict(anno);
		
		Graphs.addAllVertices(this.grafo, mappaDistretti.values());
		
		// aggiungo gli archi
		for(int i = 1; i<=7; i++) {
			for(int j=1; j<=7; j++) {
				if(i>j) {
					double peso = LatLngTool.distance(mappaDistretti.get(i).getCentro(), mappaDistretti.get(j).getCentro(), LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, mappaDistretti.get(i), mappaDistretti.get(j), peso);
				}
			}
		}
		System.out.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	 
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String getVicini() {
		String s = "";
		for(Distretto d : this.mappaDistretti.values()) {
			s += d.toString()+":\n";
			List<DistrettoVicino> temp = new LinkedList<DistrettoVicino>();
			for(Distretto vicino : Graphs.neighborListOf(this.grafo, d)) {
				DistrettoVicino dv = new DistrettoVicino(vicino, this.grafo.getEdgeWeight(this.grafo.getEdge(d, vicino)));
				temp.add(dv);
			}
			Collections.sort(temp);
			for(DistrettoVicino ddvv : temp) {
				s += ddvv.toString()+"\n";
			}
			s += "\n\n";
		}
		return s;
	}
	
}

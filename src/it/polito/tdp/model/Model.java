package it.polito.tdp.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public Graph<Integer, DefaultWeightedEdge> grafoConInteger;
	public Map<Integer, Distretto> mappaDistretti;


	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<Integer> getAnni(){
		return dao.getYears();
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleWeightedGraph<Distretto, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.grafoConInteger = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.mappaDistretti = dao.getDistrict(anno);
		
		Graphs.addAllVertices(this.grafo, mappaDistretti.values());
		Graphs.addAllVertices(this.grafoConInteger, mappaDistretti.keySet());
		// aggiungo gli archi
		for(int i = 1; i<=7; i++) {
			for(int j=1; j<=7; j++) {
				if(i>j) {
					double peso = LatLngTool.distance(mappaDistretti.get(i).getCentro(), mappaDistretti.get(j).getCentro(), LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, mappaDistretti.get(i), mappaDistretti.get(j), peso);
					Graphs.addEdgeWithVertices(this.grafoConInteger, mappaDistretti.get(i).getId(), mappaDistretti.get(j).getId(), peso);
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

	public List<Integer> getMesi(){
		return dao.getMesi();
	}
	
	public List<Integer> getGiorni(){
		return dao.getGiorni();
	}
	
	public List<Event> getEventiByData(int anno, int mese, int giorno){
		return dao.getEventiByData(anno, mese, giorno);
	}
	
	public Distretto getDistrettoMin(int anno) {
		return mappaDistretti.get(dao.getDistrettoMin(anno));
	}

	public Integer simula(Integer anno, Integer mese, Integer giorno, Integer n) {
		Simulatore sim = new Simulatore();
		sim.init(n, anno, mese, giorno, grafoConInteger);
		sim.run();
		return sim.getNumeroEventiMalGestiti();
	}
	
}

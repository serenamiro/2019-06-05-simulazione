package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;
import it.polito.tdp.model.Evento.TipoEvento;

public class Simulatore {
	
		//TIPI DI EVENTO 
	
		//1. Evento criminoso
		// 1.1 La centrale seleziona l'agente libero più vicino
		// 1.2 Se non ci sono disponibilità -> crimine mal gestito
		// 1.3 Se c'è un agente libero -> setto l'agente a occupato
		
		//2. L'agente selezionato ARRIVA sul posto
		// 2.1 Definisco quanto durerà l'intervento
		// 2.2 Controllo se il crimine è mal gestito (ritardo dell'agente)
		
		//3. Crimine TERMINATO
		// 3.1 "Libero" l'agente, che torna a essere disponibile
	
	// INPUT
	private Integer numeroAgenti;
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	
	// OUTPUT
	private Integer numeroEventiMalGestiti;
	
	// MODELLO DEL MONDO
	Graph<Integer, DefaultWeightedEdge> grafo;
	Map<Integer, Integer> agentiDisponibili; // MAPPA: distretto, numero agenti liberi
	
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> queue;
	
	public void init(Integer n, Integer anno, Integer mese, Integer giorno, Graph<Integer,DefaultWeightedEdge> grafo) {
		this.numeroAgenti = n;
		this.anno = anno;
		this.mese = mese;
		this.giorno = giorno;
		
		this.grafo = grafo;
		
		this.numeroEventiMalGestiti = 0;
		
		this.agentiDisponibili = new HashMap<Integer, Integer>();
		for(Integer distrettoid : this.grafo.vertexSet()) {
			this.agentiDisponibili.put(distrettoid, 0);
		}
		
		EventsDao dao = new EventsDao();
		Integer min = dao.getDistrettoMin(anno);
		this.agentiDisponibili.put(min, numeroAgenti);
		
		this.queue = new PriorityQueue<Evento>();
		
		for(Event e : dao.getEventiByData(anno, mese, giorno)) {
			queue.add(new Evento(TipoEvento.CRIMINE, e.getReported_date(), e));
		}	
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Evento e = queue.poll();
			processEvent(e);
		}
	}

	public Integer getNumeroAgenti() {
		return numeroAgenti;
	}

	public Integer getAnno() {
		return anno;
	}

	public Integer getMese() {
		return mese;
	}

	public Integer getGiorno() {
		return giorno;
	}

	public Integer getNumeroEventiMalGestiti() {
		return numeroEventiMalGestiti;
	}

	

	public Map<Integer, Integer> getAgentiDisponibili() {
		return agentiDisponibili;
	}

	public PriorityQueue<Evento> getQueue() {
		return queue;
	}

	private void processEvent(Evento e) {
		switch(e.getTipo()){
		case CRIMINE:
			System.out.println("NUOVO CRIMINE! "+e.getCrimine().getIncident_id());
			
			// Devo trovare l'agente più vicino
			Integer partenza = null;
			partenza = cercaAgente(e.getCrimine().getDistrict_id());
			
			if(partenza != null) {
				this.agentiDisponibili.put(partenza, this.agentiDisponibili.get(partenza)-1);
				
				Double distanza;
				if(partenza.equals(e.getCrimine().getDistrict_id())) {
					distanza = 0.0;
				} else {
					distanza = grafo.getEdgeWeight(this.grafo.getEdge(partenza, e.getCrimine().getDistrict_id()));
				}
				
				Long secondi = (long)((distanza*1000)/(60/3.6));
				this.queue.add(new Evento(TipoEvento.ARRIVA_AGENTE, e.getData().plusSeconds(secondi), e.getCrimine()));
				
			} else {
				// non ci sono agenti dispo
				System.out.println("CRIMINE mal gestito.\n");
				this.numeroEventiMalGestiti++;
			}
			break;
			
			
		case ARRIVA_AGENTE:
			System.out.println("Arriva agente per crimine.\n");
			Long durata = getDurata(e.getCrimine().getOffense_category_id());
			this.queue.add(new Evento(TipoEvento.GESTITO, e.getData().plusSeconds(durata), e.getCrimine()));
			if(e.getData().isAfter(e.getCrimine().getReported_date().plusMinutes(15))) {
				System.out.println("CRIMINE " + e.getCrimine().getIncident_id() + " MAL GESTITO!");
				this.numeroEventiMalGestiti++;
			}
			break;
			
		
		case GESTITO:
			System.out.println("CRIMINE " + e.getCrimine().getIncident_id() + " GESTITO");
			this.agentiDisponibili.put(e.getCrimine().getDistrict_id(), this.agentiDisponibili.get(e.getCrimine().getDistrict_id())+1);
			break;
		}
	}
	
	/**
	 * Metodo per calcolare la durata di un crimine: in un caso è randomica, nell'altro deterministica
	 * @param offense_category_id
	 * @return
	 */
	private Long getDurata(String offense_category_id) {
		if(offense_category_id.equals("all_other_crimes")) {
			Random r = new Random();
			if(r.nextDouble() > 0.5)
				return Long.valueOf(2*60*60);
			else
				return Long.valueOf(1*60*60);
		} else {
			return Long.valueOf(2*60*60);
		}
	}
	
	/**
	 * Metodo per cercare il distretto e l'agente più vicini al crimine
	 * @param district_id
	 * @return
	 */
	private Integer cercaAgente(Integer district_id) {
		Double distanza = Double.MAX_VALUE;
		Integer distretto = null;
		
		for(Integer d : this.agentiDisponibili.keySet()) {
			if(this.agentiDisponibili.get(d) > 0) {
				if(district_id.equals(d)) {
					distanza = 0.0;
					distretto = d; 
				} else if(this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d)) < distanza) {
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d));
					distretto = d;
				}
			}
		}
		return distretto;
	}
	

}

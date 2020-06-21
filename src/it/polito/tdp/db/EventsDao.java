package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.model.Distretto;
import it.polito.tdp.model.Event;

public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getYears(){
		String sql = "SELECT DISTINCT YEAR(reported_date) as anno " + 
				"FROM events";
		
		List<Integer> years = new LinkedList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				years.add(res.getInt("anno"));
			}
			
			conn.close();
			Collections.sort(years);
			return years;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Map<Integer, Distretto> getDistrict(int anno){
		String sql = "SELECT district_id as id, AVG(geo_lat) AS lat_centro, AVG(geo_lon) AS lon_centro " + 
						"				FROM events " + 
						"				WHERE YEAR(reported_date) = ? " + 
						"				GROUP BY district_id ";
		
		 Map<Integer, Distretto> districts = new HashMap<Integer, Distretto>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Distretto d = new Distretto(res.getInt("id"), res.getDouble("lat_centro"), res.getDouble("lon_centro"));
				districts.put(d.getId(), d);
			}
			
			conn.close();
			return districts;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

	public List<Integer> getMesi() {
		String sql = "SELECT DISTINCT MONTH(reported_date) as anno " + 
				"FROM events";
		
		List<Integer> years = new LinkedList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				years.add(res.getInt("anno"));
			}
			
			conn.close();
			//Collections.sort(years);
			return years;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getGiorni() {
		String sql = "SELECT DISTINCT DAY(reported_date) as anno " + 
				"FROM events";
		
		List<Integer> years = new LinkedList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				years.add(res.getInt("anno"));
			}
			
			conn.close();
			//Collections.sort(years);
			return years;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Event> getEventiByData(int anno, int mese, int giorno){
		String sql = "SELECT * FROM events WHERE YEAR(reported_date)=? AND MONTH(reported_date)=? AND DAY(reported_date)=? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Integer getDistrettoMin(Integer anno) {
		String sql = "SELECT district_id " + 
				"FROM events " + 
				"WHERE Year(reported_date) = ? " + 
				"GROUP BY district_id " + 
				"ORDER BY COUNT(*) ASC " + 
				"LIMIT 1";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				conn.close();
				return res.getInt("district_id");
			} else {
				conn.close();
				return null;
			}
			
			
		} catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

}

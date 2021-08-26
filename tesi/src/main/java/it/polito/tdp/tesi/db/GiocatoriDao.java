package it.polito.tdp.tesi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.tesi.model.Collegamenti;
import it.polito.tdp.tesi.model.Giocatore;
import it.polito.tdp.tesi.model.Ruolo;

public class GiocatoriDao {
	

	public List<Ruolo> getRuoli(){
		
		String sql = "SELECT * FROM ruolo ORDER BY ruoloID ";
		
		List<Ruolo> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql); 
			
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				
				result.add(new Ruolo(res.getInt("ruoloID"), res.getString("abbreviazione"), res.getString("nome")));
			}
			conn.close();
			return result; 
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	

	public List<String> getModuli(){
		
		String sql = "SELECT DISTINCT modulo FROM affinita";
		
		List<String> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql); 
			
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				
				result.add(res.getString("modulo"));
			}
			conn.close();
			return result; 
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}


	public List<Collegamenti> getCollegamenti(String modulo){
		
		String sql = "SELECT * "
				+ "FROM affinita "
				+ "WHERE modulo = ? ";
		
		List<Collegamenti> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql); 
			
			st.setString(1, modulo);
			
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				
				result.add(new Collegamenti(res.getString("modulo"), res.getInt("giocatore1"), res.getInt("giocatore2")));
			}
			conn.close();
			return result; 
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	

	public List<Giocatore> getGiocatori(){
		
		String sql = "SELECT *  "
				+ "FROM giocatori "
				+ "ORDER BY overall DESC ";
		
		List<Giocatore> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql); 
			
			ResultSet res= st.executeQuery();
			
			while(res.next()) {
				
				result.add(new Giocatore(res.getInt("giocatoreID"), res.getString("nome"), res.getString("revisione"), 
						res.getInt("overall"), res.getString("club"), res.getString("lega"), res.getString("nazionalita"), 
						res.getString("posizione"), res.getInt("prezzo_ps4")));
			}
			conn.close();
			return result; 
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}

}

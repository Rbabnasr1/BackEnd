package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class PlaceModel {
	private String desc;
	private double lon;
	private double lat;
	private Integer id;
	private String name;
private static PlaceModel place = new  PlaceModel();
	
	private  PlaceModel(){}
	
	public static  PlaceModel getInstance ()
	{
	return place;	
	}
	
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public static boolean addPlace(String name,String descr) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into places (`name`,`description`,`lat`,`long`) VALUES(?,?,0,0)";
			 System.out.println(name + descr);

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, descr);
			stmt.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
public static boolean savePlace(String user , String place ) {
		
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "INSERT INTO `saveplace`(`user`, `place`) VALUES (?,?)";
			
			PreparedStatement stmt,stmt1;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
			stmt.setString(2, place);
		
			stmt.executeUpdate();
			
			String sql4="INSERT INTO `history`(`user`, `action`) VALUES (?,'save "+place+" in you places list')";
			PreparedStatement stmt4;
			stmt4 = conn.prepareStatement(sql4);
			stmt4.setString(1, user);
			stmt4.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

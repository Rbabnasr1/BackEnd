package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class UserModel {

	
	private String name;
	private String email;
	private String pass;
	private Integer id;
	private Double lat;
	private Double lon;
	
	public String getPass(){
		return pass;
	}
	
	public void setPass(String pass){
		this.pass = pass;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public static UserModel addNewUser(String name, String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Insert into users (`name`,`email`,`password`) VALUES  (?,?,?)";
			// System.out.println(sql);

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, pass);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = email;
				user.pass = pass;
				user.name = name;
				user.lat = 0.0;
				user.lon = 0.0;
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static UserModel login(String email, String pass) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from users where `email` = ? and `password` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, pass);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.id = rs.getInt(1);
				user.email = rs.getString("email");
				user.pass = rs.getString("password");
				user.name = rs.getString("name");
				user.lat = rs.getDouble("lat");
				user.lon = rs.getDouble("long");
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*public static UserModel follow(int id) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select 'name' from users where `id` = ? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
		
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.name = rs.getString("name");
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
*/
	public static boolean updateUserPosition(Integer id, Double lat, Double lon) {
		try{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Update users set `lat` = ? , `long` = ? where `id` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}

	
	public static UserModel follow(int id_follower, int id_following) {
		//System.out.println(id_follower + " " + id_following);
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from users where `id` = ? ";
			
			PreparedStatement stmt,stmt1;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id_follower);
			//stmt.setInt(2, id2);
		
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.name = rs.getString("name");
				
				String sql2="insert into follow (`follower_id`,`following_id`)values (?,?) ";
				stmt1 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
				stmt1.setInt(1,id_follower);
				stmt1.setInt(2,id_following);
				stmt1.executeUpdate();
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void unFollow(int id_follower, int id_following) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "delete from `follow` where `follower_id` = ? and `following_id`=? ";
			
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id_follower);
			stmt.setInt(2, id_following);
		
			 stmt.executeUpdate();
			
			//return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	return null;
		}


	public static ArrayList getFollowingList(int id_follower)
	{
		try {
			ArrayList re = new ArrayList<String>();
			Connection conn = DBConnection.getActiveConnection();
			String sql = "select name from users where id in (select following_id from follow where follower_id =?)";
			
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id_follower);
			

			ResultSet rs = stmt.executeQuery();
			UserModel user = new UserModel();
			
			while(rs.next()){
			user.name=rs.getString("name");
			re.add(user.name);
			}
			return re;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return null;
		}

	public static UserModel getLastPosition(int id_follower) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from users where `id` = ? ";
			
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id_follower);
			
		
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				UserModel user = new UserModel();
				user.name=rs.getString("name");
				user.lat = rs.getDouble("lat");
				user.lon = rs.getDouble("long");
		
					return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		
	}
	
}
	


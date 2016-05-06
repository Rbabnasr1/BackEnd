package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import com.mysql.jdbc.Statement;

public class UserModel {
	
	private String name;
	private String email;
	private String pass;
	private Integer id;
	private Double lat;
	private Double lon;
	
	///////////singlton////////////////////
	private static UserModel user = new UserModel();
	
	private UserModel(){}
	
	public static UserModel getInstance ()
	{
	return user;	
	}
	///////////////////////////////////////
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
			
			//get follower name
			String sql = "Select * from users where `id` = ? ";
			PreparedStatement stmt,stmt1, stmt3;
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
				
				// get following name
				String sql3 = "Select * from users where `id` = ? ";
				stmt3 = conn.prepareStatement(sql3);
				stmt3.setInt(1, id_following);
				ResultSet rs3 = stmt3.executeQuery();
				if (rs3.next()) {
					user.email = rs3.getString("name");
				
				//saving in history	
				String sql4="INSERT INTO `history`(`user`, `action`) VALUES ('"+user.getName()+"'"
						+ ",'follow "+ user.getEmail()+"')";
				PreparedStatement stmt4;
				stmt4 = conn.prepareStatement(sql4);
				stmt4.executeUpdate();
				
				return user;
			}}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    public static ArrayList  getPlacesforAuser(String user) {
		
		try {

			ArrayList re = new ArrayList<String>();
			Connection conn = DBConnection.getActiveConnection();
			String sql = "SELECT `place` FROM `saveplace` WHERE `user` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
			

			ResultSet rs = stmt.executeQuery();
			UserModel user1 = new UserModel();
			
			while(rs.next()){
			user1.name=rs.getString("place");
			re.add(user1.name);
			}
			return re;
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return null;
		}
    public static ArrayList  getUsersforAplace() {
	try {
		ArrayList re = new ArrayList<String>();
		Connection conn = DBConnection.getActiveConnection();
		String sql = "SELECT `place`,`user` FROM `saveplace` order by `place`";
		PreparedStatement stmt;
		stmt = conn.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		UserModel user1 = new UserModel();
		
		while(rs.next()){
		user1.name=rs.getString("place");
		user1.email=rs.getString("user");
		re.add(user1.name + " -> " + user1.email);
		}
		return re;
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
return null;
	}
    public static ArrayList countUsersForEachPlace() {
	try {
		ArrayList re = new ArrayList<String>();
		Connection conn = DBConnection.getActiveConnection();
		String sql = "SELECT `place`,count(user) FROM `saveplace` group by `place`";
		PreparedStatement stmt;
		stmt = conn.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		UserModel user1 = new UserModel();
		
		while(rs.next()){
		user1.name=rs.getString("place");
		user1.email=rs.getString("count(user)");
		System.out.println( user1.getEmail() );
		re.add(user1.getName() + " -> " + user1.getEmail());
		}
		return re;
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
			/*if (rs.next()) {
				user.id= rs.getInt("following_id");
				String sql1 ="select `name` from users where id = "
				+user.id;
				PreparedStatement stmt1;
				stmt1 = conn.prepareStatement(sql1);
				ResultSet rs1 = stmt1.executeQuery();
				if(rs1.next())
				{
					UserModel user1 = new UserModel();
					user1.name= rs.getString("name");
					re.add(user1.name);
				}
			}*/
			return re;
			}} catch (SQLException e) {
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
	public static ArrayList<String> showhomePage(String user) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "SELECT * FROM `check-in` WHERE user=?";
			PreparedStatement stmt , stmt1,stmt2 ;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
		                	//System.out.println(user );
			ResultSet rs = stmt.executeQuery();
			ArrayList re = new ArrayList<String>();
			while (rs.next()) {
				UserModel user1 = new UserModel();
				user1.name=rs.getString("place");
				re.add("you checked in places: " + user1.getName());
				System.out.println(re);
			
			System.out.println("rbab hna :s     "+re);
			String sql1="select `user` ,`place` from `check-in` where `user` in "
					+ "(select  `name` from `users` where `id` in ("
               +" select `following_id` from `follow` where `follower_id` in ("
                             +"select `id` from `users` where `name`= ?)))";
			
				stmt1 = conn.prepareStatement(sql1);	
				stmt1.setString(1, user);
				ResultSet rs1 = stmt1.executeQuery();
				UserModel user2 = new UserModel();
				while (rs1.next()) {
					
					user2.name=rs1.getString("user");
					//System.out.println("selkoooo +"+user2.name);
					user2.email=rs1.getString("place");
					re.add("your friend " + user2.getName() + " checked in " + user2.getEmail());
					System.out.println(re);
				
				
					String sql2="select `user` ,`place` from `saveplace` where `user` in"
							+ "(select  `name` from `users` where `id` in "
							+ "(select `following_id` from `follow` where `follower_id` in"
							+ "(select `id` from `users` where `name`= ?)))";
					stmt2 = conn.prepareStatement(sql2);	
					stmt2.setString(1, user);
					ResultSet rs2 = stmt2.executeQuery();
					while (rs2.next()) {
						UserModel user3 = new UserModel();
						user3.name=rs2.getString("user");
						user3.email=rs2.getString("place");
						re.add("your friend " +user3.getName() + " saved places " + user3.getEmail());
						System.out.println(re);
					}}}
					String sql4="INSERT INTO `history`(`user`, `action`) VALUES ('"+user+"','Show home page')";
					PreparedStatement stmt4;
					stmt4 = conn.prepareStatement(sql4);
					stmt4.executeUpdate();	
					System.out.println("....j....ruby .............");
					
					
		return re;
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
		
}
	

 
package com.models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;
public class checkin {
	private String user;
	private String comment;
	private Integer like;
	private String place;
	//notifications
	private String user1;
	private String notifiedUser;
	private String type;
private static checkin check= new checkin();
	
	private checkin (){}
	
	public static checkin  getInstance ()
	{
	return check;	
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Integer getLike() {
		return like;
	}
	public void setLike(Integer like) {
		this.like = like;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public static boolean checkin(String user,String place) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql ="INSERT INTO `check-in`(`user`, `place`, `comment`, `like`) VALUES (?,?,'',0)";
			System.out.println(user+place);

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user);
			stmt.setString(2, place);
			stmt.executeUpdate();
			
			String sql4="INSERT INTO `history`(`user`, `action`) VALUES (?,'checked in "+place+"')";
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
	public static boolean likeCheckIn(String notifieduser,String place,String user ) {
			try {
				Connection conn = DBConnection.getActiveConnection();
				String sql = "SELECT `like` FROM `check-in` WHERE user =? and place = ?";
				PreparedStatement stmt , stmt1 , stmt2;
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, notifieduser);
				stmt.setString(2, place);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					checkin check = new checkin();
					check.like = rs.getInt("like");
					int likes = check.getLike();
					check.setLike(likes+1);
					
					String sql1 = "UPDATE `check-in` SET `like`= "+ check.getLike() +
							      " WHERE `user`= '"+ notifieduser +"' and `place`= '"+ place +"'";
					
					stmt1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);	
					stmt1.executeUpdate();
					
					String sql2 = "INSERT INTO `notification`(`notifiedUser`,`type`)VALUES ('"+notifieduser+"','like from "+user+"')";                           
					stmt1 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);	
					stmt1.executeUpdate();
					
					
					String sql4="INSERT INTO `history`(`user`, `action`) VALUES "
							+ "(?,'liked a check in of "+notifieduser+" in "+place+"')";
					PreparedStatement stmt4;
					stmt4 = conn.prepareStatement(sql4);
					stmt4.setString(1, user);
					stmt4.executeUpdate();
					return true;
     
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	
	public static boolean commentCheckIn(String notifieduser,String place, String coment1 , String user) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select `comment` from `check-in` where `user` = ? and `place` = ?";
			PreparedStatement stmt , stmt1 , stmt2;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, notifieduser);
			stmt.setString(2, place);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				checkin check = new checkin();
				check.comment = rs.getString("comment");
				String coment = check.getComment();
				check.setComment(coment + " "+ coment1); //// append on last comments
				
				String sql1 = "UPDATE `check-in` SET `comment`= '"+ check.getComment()+
						      "' WHERE `user`= '"+notifieduser+"'and `place`= '"+ place +"'";
				
				stmt1 = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);	
				stmt1.executeUpdate();
				
				String sql2 = "INSERT INTO `notification`(`notifiedUser`,`type`)VALUES ('"+notifieduser+"','"+user+" comment "+coment1+"')";                           
				stmt1 = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);	
				stmt1.executeUpdate();
				
				String sql4="INSERT INTO `history`(`user`, `action`) VALUES "
						+ "(?,'commented on check in of "+notifieduser+" in "+place+"')";
				PreparedStatement stmt4;
				stmt4 = conn.prepareStatement(sql4);
				stmt4.executeUpdate();
				
				
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static ArrayList<String> GetAllNotifications(String user) {
		try {
			 System.out.println("back ....ya robab..."  );
		     ArrayList re = new ArrayList<String>();
			Connection conn = DBConnection.getActiveConnection();
			String sql = "SELECT type FROM `notification` where notifieduser = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,user);
			
			ResultSet rs = stmt.executeQuery();
			checkin  check = new checkin();
			
			while(rs.next()){
			check.type=rs.getString("type");
			
			re.add(check.type);
			System.out.print("hna el notifi   type   "+re);
			
			String sql4="INSERT INTO `history`(`user`, `action`) VALUES (?,'Get all notifications')";
			PreparedStatement stmt4;
			stmt4 = conn.prepareStatement(sql4);
			stmt4.setString(1, user);
			stmt4.executeUpdate();
			 System.out.println("back ......." + re);
				
			}
			return re;
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return null;
		}
	public static ArrayList<String> respondToNotification(String user,String place) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "Select * from `check-in` where `user` = ? and `place` = ?";
			PreparedStatement stmt , stmt1 ;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
			stmt.setString(2, place);
			ResultSet rs = stmt.executeQuery();
			ArrayList re = new ArrayList<String>();
			while (rs.next()) {
				checkin check = new checkin();
				check.user = rs.getString("user");
				check.place = rs.getString("place");
				check.comment = rs.getString("comment");
				check.like = rs.getInt("like");
				String coment = check.getComment();
				
				re.add(check.getUser() + " checked in " + check.getPlace() + " have " + check.getLike()
						+ " likes and comments are: " + check.getComment() );
			
			String sql4="INSERT INTO `history`(`user`, `action`) VALUES (?,'respond to notification')";
			PreparedStatement stmt4;
			stmt4 = conn.prepareStatement(sql4);
			stmt4.setString(1, user);
			stmt4.executeUpdate();
			}
			
			return re;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<String> HistoryOfActions(String user) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "SELECT action FROM `history` where user = ?";
			PreparedStatement stmt  ;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
			
			ResultSet rs = stmt.executeQuery();
			ArrayList re = new ArrayList<String>();
			while (rs.next()) {
				checkin check = new checkin();
				check.user = rs.getString("action");
				re.add(check.getUser());
			}
			return re;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

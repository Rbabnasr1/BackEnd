package com.models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class UserSorter implements SortModel{

	@Override
	public  ArrayList<String> sorthomePage(String user) {
		try {
			Connection conn = DBConnection.getActiveConnection();
			String sql = "SELECT `user`,'check-in',`place`  FROM `check-in` WHERE user= ?"
					+ " union"
					+ " select `user` ,'check-in', `place` from `check-in` where `user` in "
					+ "(select  `name` from `users` where `id` in"
					+ " (select `following_id` from `follow` where `follower_id` in "
					+ "(select `id` from `users` where `name`= ?))) "
					+ "union "
					+ "select `user` ,'saved place',`place` from `saveplace` where `user` in "
					+ "(select  `name` from `users` where `id` in ("
					+ "select `following_id` from `follow` where `follower_id` in "
					+ "(select `id` from `users` where `name`= ?)))"
					+ "order by `user`";
			PreparedStatement stmt , stmt1,stmt2 ;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user);
			stmt.setString(2, user);
			stmt.setString(3, user);
		                	//System.out.println(user );
			ResultSet rs = stmt.executeQuery();
			ArrayList re = new ArrayList<String>();
			while(rs.next())
			{
				re.add(rs.getString("user") +" "+rs.getString("check-in")+" "+rs.getString("place"));
			}
			
			//System.out.println("sort ya gama3a " + re);
					
		return re;
	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}

}

package com.models;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
	private static Connection connection = null;

	public static Connection getActiveConnection() {
		/*String host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		String port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		System.out.println(host);*/
		try {
			Class.forName("com.mysql.jdbc.Driver");

		//	connection = DriverManager
		//	.getConnection("jdbc:mysql://127.3.236.2:3306/fcisquare?"
			//	+ "user=adminnTgjMWM&password=ss5_AUqs9v5U&characterEncoding=utf8");
		connection = DriverManager
		.getConnection("jdbc:mysql://localhost:3306/fcisquare?"
				+ "user=se2&password=root&characterEncoding=utf8");
		//connection = DriverManager
		
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*public static void main(String[] args) {
		//System.out.println("Hello ");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/fcisquare?"
							+ "user=se2&password=root&characterEncoding=utf8");
			
		} catch (ClassNotFoundException e) {
			System.out.println("Hello1 ");
			e.printStackTrace();
			
		} catch (SQLException e) {
			System.out.println("Hello 2");
			e.printStackTrace();
		}
	}*/
}

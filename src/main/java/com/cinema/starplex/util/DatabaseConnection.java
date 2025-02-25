//package com.cinema.starplex.util;
//
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnection {
//    private static final String URL = "jdbc:mysql://localhost:3306/@@@@";
//    private static final String USER = "root";
//    private static final String PASSWORD = "";
//
//    private DatabaseConnection conn;
//
//    public DatabaseConnection() {
//        try {
//            this.conn = (DatabaseConnection) DriverManager.getConnection(URL, USER, PASSWORD);
//        } catch (SQLException e) {
//            System.out.println("Database connection failed!");
//            e.printStackTrace();
//        }
//    }
//    public DatabaseConnection getConn() {
//        return conn;
//    }
//
//
//}

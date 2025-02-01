package com.rnzonly;

import java.sql.*;

public class DatabaseHandlerLab {
  private Connection sqlCon;

  DatabaseHandlerLab(String patternDatabase) {
    String connStr = "jdbc:sqlite:resources/" + patternDatabase;
    try {
      sqlCon = DriverManager.getConnection(connStr);
    } catch (SQLException e) {
      System.err.println("Failed to create connection");
      System.err.println(e.toString());
    }
  }

  public static void main(String[] args) { 
    DatabaseHandlerLab dbh = new DatabaseHandlerLab("students.db");
    System.out.println("Hello World");
  }
}

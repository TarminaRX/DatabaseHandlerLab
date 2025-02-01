package com.rnzonly;

import java.sql.*;

public class DatabaseHandlerLab {
  private Connection sqlCon;
  private PreparedStatement queryToExecute;

  DatabaseHandlerLab(String patternDatabase) {
    String connStr = "jdbc:sqlite:resources/" + patternDatabase;
    try {
      sqlCon = DriverManager.getConnection(connStr);
    } catch (SQLException e) {
      System.err.println("Failed to create connection");
      System.err.println(e.toString());
    }
  }

  private String formatSqlString(String fMat) { return "\"" + fMat + "\""; }

  private Student resultToStudent(ResultSet stud) throws SQLException {
    Student bufStud = new Student(
        stud.getString("student_number"), stud.getString("student_fname"),
        stud.getString("student_mname"), stud.getString("student_lname"),
        stud.getString("student_sex"), stud.getString("student_birth"),
        stud.getString("student_department"), stud.getString("student_address"),
        stud.getInt("student_start"), stud.getInt("student_units"));
    return bufStud;
  }

  void initializeStudents() throws SQLException {
    queryToExecute = sqlCon.prepareStatement("DROP TABLE IF EXISTS Students");
    String sqlTwo = "CREATE TABLE Students (\n"
                    + "    student_number TEXT NOT NULL,\n"
                    + "    student_fname TEXT NOT NULL,\n"
                    + "    student_mname TEXT,\n"
                    + "    student_lname TEXT NOT NULL,\n"
                    + "    student_sex TEXT NOT NULL,\n"
                    + "    student_birth TEXT NOT NULL,\n"
                    + "    student_start INTEGER NOT NULL,\n"
                    + "    student_department TEXT NOT NULL,\n"
                    + "    student_units INTEGER NOT NULL,\n"
                    + "    student_address TEXT,\n"
                    +
                    "    CONSTRAINT Students_PK PRIMARY KEY (student_number)\n"
                    + ");";

    queryToExecute.executeUpdate();
    queryToExecute = sqlCon.prepareStatement(sqlTwo);
    queryToExecute.executeUpdate();
  }

  Student getStudent(String studentNumber) throws SQLException {
    queryToExecute = sqlCon.prepareStatement(
        "SELECT * FROM Students s WHERE s.student_number = ?");
    queryToExecute.setString(1, studentNumber);
    ResultSet r = queryToExecute.executeQuery();
    while (r.next()) {
      return resultToStudent(r);
    }
    return null;
  }

  Student getStudent(String studentFname, String studentMname, String studentLname) { 
    return null;
  }

  public static void main(String[] args) {
    try {
      DatabaseHandlerLab dbh = new DatabaseHandlerLab("students.db");
      //dbh.initializeStudents();
      Student a = dbh.getStudent("12340105678");
      System.out.println("Hello World");
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}

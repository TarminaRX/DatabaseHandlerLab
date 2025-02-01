package com.rnzonly;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
  
  private void checkStudentNumber(String number) throws Exception {
    boolean error = false;
    if (number.length() != 11) {
      throw new RuntimeException("Not a valid student number");
    }

    for (int i = 0; i < number.length(); i++) {
      if (!(number.charAt(i) >= 48 && number.charAt(i) <= 57)) {
        error = true;
        break;
      }
      if ((i == 4 || i == 6) && number.charAt(i) != '0') {
        error = true;
        break;
      }
      if (i == 5 && number.charAt(i) != '1') {
        error = true;
        break;
      }
    }

    if (error) {
      throw new RuntimeException("Not a valid student number");
    }
  }

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

  Student getStudent(String studentFname, String studentMname,
                     String studentLname) throws SQLException {
    queryToExecute = sqlCon.prepareStatement(
        "SELECT * FROM Students s WHERE s.student_fname = ? AND "
        + "s.student_mname = ? AND s.student_lname = ?");
    queryToExecute.setString(1, studentFname);
    queryToExecute.setString(2, studentMname);
    queryToExecute.setString(3, studentLname);
    ResultSet r = queryToExecute.executeQuery();
    while (r.next()) {
      return resultToStudent(r);
    }
    return null;
  }

  Student[] getStudents() throws SQLException {
    queryToExecute = sqlCon.prepareStatement("SELECT * FROM Students");
    ResultSet r = queryToExecute.executeQuery();
    List<Student> tempL = new ArrayList<>();
    while (r.next()) {
      tempL.add(resultToStudent(r));
    }
    return tempL.toArray(new Student[0]);
  }

  boolean removeStudent(String studentNumber) throws SQLException {
    queryToExecute = sqlCon.prepareStatement("DELETE FROM Students s WHERE s.student_number = ?");
    queryToExecute.setString(1, studentNumber);
    return false;
  } 




  //void checkDateNumber(String dateStr) throws Exception {
  //  String[] splittedStr = dateStr.trim().split("-");
  //  if 
  //}

  boolean insertStudent(Student newStudent) { return false; }

  public static void main(String[] args) {
    try {
      DatabaseHandlerLab dbh = new DatabaseHandlerLab("students.db");
      // dbh.initializeStudents();
      Student a = dbh.getStudent("12340105678");
      Student[] b = dbh.getStudents();
      System.out.println("Hello World");
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}

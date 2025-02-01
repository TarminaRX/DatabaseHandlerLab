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

  private void checkStudentSex(String sexStr) throws Exception {
    if (sexStr.length() > 1 ||
        (sexStr.charAt(0) != 'M' && sexStr.charAt(0) != 'F')) {
      throw new RuntimeException("Invalid Sex!");
    }
  }

  private void checkValidYear(Integer year) {
    if (year < 1000 || year > 9999) {
      throw new RuntimeException("Not a valid year!");
    }
  }

  private void checkDateNumber(String dateStr) throws Exception {
    String[] splittedStr = dateStr.trim().split("-");
    if (splittedStr.length != 3) {
      throw new RuntimeException("Not a valid date format!");
    }
    checkValidYear(Integer.valueOf(splittedStr[0]));
    int month = Integer.parseInt(splittedStr[1]);
    int date = Integer.parseInt(splittedStr[2]);
    if ((month < 0 || month > 12) || (date < 1 || date > 31)) {
      throw new RuntimeException("Not a valid date format!");
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
        stud.getInt("student_start"), stud.getString("student_department"),
        stud.getInt("student_units"), stud.getString("student_address"));
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
    queryToExecute = sqlCon.prepareStatement(
        "DELETE FROM Students WHERE student_number = ?");
    queryToExecute.setString(1, studentNumber);
    return (queryToExecute.executeUpdate() > 0) ? true : false;
  }

  boolean getStudentsByYear(int year) throws SQLException {
    queryToExecute = sqlCon.prepareStatement(
        "SELECT * FROM Students s WHERE s.student_start LIKE ?");
    queryToExecute.setInt(1, year);
    ResultSet rss = queryToExecute.executeQuery();
    return rss.next();
  }

  boolean updateStudentInfo(String studentNumber, Student studentInfo)
      throws SQLException {
    queryToExecute = sqlCon.prepareStatement(
        "UPDATE Students SET student_fname = ?, student_mname = ?, "
        + "student_lname = ?, student_department = ?, student_address = ? "
        + "WHERE student_number = ?");
    queryToExecute.setString(1, studentInfo.getStudent_fname());
    queryToExecute.setString(2, studentInfo.getStudent_mname());
    queryToExecute.setString(3, studentInfo.getStudent_lname());
    queryToExecute.setString(4, studentInfo.getStudent_department());
    queryToExecute.setString(5, studentInfo.getStudent_address());
    queryToExecute.setString(6, studentNumber);
    return (queryToExecute.executeUpdate() > 0) ? true : false;
  }

  boolean updateStudentUnits(int subtractedUnits, String studentNumber)
      throws SQLException {
    queryToExecute = sqlCon.prepareStatement(
        "UPDATE Students SET student_units = ? WHERE student_number = ?");
    queryToExecute.setInt(1, subtractedUnits);
    queryToExecute.setString(2, studentNumber);
    return (queryToExecute.executeUpdate() > 0) ? true : false;
  }

  boolean insertStudent(Student newStudent) throws Exception {
    checkStudentNumber(newStudent.getStudent_number());
    checkStudentSex(newStudent.getStudent_sex());
    checkDateNumber(newStudent.getStudent_birth());
    checkValidYear(newStudent.getStudent_start());
    if (newStudent.getStudent_fname() == null ||
        newStudent.getStudent_mname() == null ||
        newStudent.getStudent_lname() == null ||
        newStudent.getStudent_department() == null) {
      throw new RuntimeException("Name or department can't be null");
    }
    queryToExecute = sqlCon.prepareStatement(
        "INSERT INTO Students ( student_number , student_fname , "
        + "student_mname , student_lname , student_sex , student_birth , "
        + "student_start , student_department , student_units , "
        + "student_address ) values ( ? , ? , ?, ? , ?, ? , ?, ?, ? , ?);");
    queryToExecute.setString(1, newStudent.getStudent_number());
    queryToExecute.setString(2, newStudent.getStudent_fname());
    queryToExecute.setString(3, newStudent.getStudent_mname());
    queryToExecute.setString(4, newStudent.getStudent_lname());
    queryToExecute.setString(5, newStudent.getStudent_sex());
    queryToExecute.setString(6, newStudent.getStudent_birth());
    queryToExecute.setInt(7, newStudent.getStudent_start());
    queryToExecute.setString(8, newStudent.getStudent_department());
    queryToExecute.setInt(9, newStudent.getStudent_units());
    queryToExecute.setString(10, newStudent.getStudent_address());
    return (queryToExecute.executeUpdate() > 0) ? true : false;
  }

  public static void main(String[] args) {
    try {
      DatabaseHandlerLab dbh = new DatabaseHandlerLab("students.db");
      Student testStud =
          new Student("88880102417", "Rob", "Mahal", "Lusel", "M", "2006-12-28",
                      2017, "CICS", 17, "Valenzuela");
      Student testStud2 =
          new Student("17280105289", "Dre", "Nathan", "Dela Cruz", "M",
                      "2004-09-23", 2019, "CICS", 21, "Makati");
      dbh.initializeStudents();
      dbh.insertStudent(testStud);
      dbh.insertStudent(testStud2);

      // Get student methods
      dbh.getStudent("88880102417");
      Student[] listS = dbh.getStudents();
      listS[0].getStudent_fname();
      dbh.getStudentsByYear(2019);

      // Update student info
      testStud2.setStudent_address("Makati 2");

      dbh.updateStudentInfo(testStud2.getStudent_number(), testStud2);

      // Update student units
      dbh.updateStudentUnits(21, testStud.getStudent_number());

      // Delete records
      dbh.removeStudent(testStud.getStudent_number());
      dbh.removeStudent(testStud2.getStudent_number());
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}

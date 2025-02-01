package com.rnzonly;

public class Student {
  private String student_number, student_fname, student_mname, student_lname, student_sex, student_birth, student_department, student_address;
  private Integer student_start, student_units;
  public Student(String student_number, String student_fname, String student_mname, String student_lname,
      String student_sex, String student_birth, Integer student_start, String student_department, Integer student_units,
      String student_address) {
    this.student_number = student_number;
    this.student_fname = student_fname;
    this.student_mname = student_mname;
    this.student_lname = student_lname;
    this.student_sex = student_sex;
    this.student_birth = student_birth;
    this.student_start = student_start;
    this.student_department = student_department;
    this.student_units = student_units;
    this.student_address = student_address;
  }
  public String getStudent_number() {
    return student_number;
  }
  public void setStudent_number(String student_number) {
    this.student_number = student_number;
  }
  public String getStudent_fname() {
    return student_fname;
  }
  public void setStudent_fname(String student_fname) {
    this.student_fname = student_fname;
  }
  public String getStudent_mname() {
    return student_mname;
  }
  public void setStudent_mname(String student_mname) {
    this.student_mname = student_mname;
  }
  public String getStudent_lname() {
    return student_lname;
  }
  public void setStudent_lname(String student_lname) {
    this.student_lname = student_lname;
  }
  public String getStudent_sex() {
    return student_sex;
  }
  public void setStudent_sex(String student_sex) {
    this.student_sex = student_sex;
  }
  public String getStudent_birth() {
    return student_birth;
  }
  public void setStudent_birth(String student_birth) {
    this.student_birth = student_birth;
  }
  public String getStudent_department() {
    return student_department;
  }
  public void setStudent_department(String student_department) {
    this.student_department = student_department;
  }
  public String getStudent_address() {
    return student_address;
  }
  public void setStudent_address(String student_address) {
    this.student_address = student_address;
  }
  public Integer getStudent_start() {
    return student_start;
  }
  public void setStudent_start(Integer student_start) {
    this.student_start = student_start;
  }
  public Integer getStudent_units() {
    return student_units;
  }
  public void setStudent_units(Integer student_units) {
    this.student_units = student_units;
  }
}

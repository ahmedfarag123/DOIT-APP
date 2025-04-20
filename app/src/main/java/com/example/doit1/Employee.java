package com.example.doit1;

public class Employee {

    private String employeeId;
    private String name;
    private String email;
    private String mobile;
    private String nationality;
    private String employeeCode;

    public Employee(String employeeId, String name, String email, String mobile, String nationality, String employeeCode) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.nationality = nationality;
        this.employeeCode = employeeCode;
    }

    public Employee(String employeeId, String name, String nationality) {
        this.employeeId = employeeId;
        this.name = name;
        this.nationality = nationality;
    }

    public Employee() {
    }

    public Employee(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }
}

package com.example.tlulostandclaim.data.model;

public class User {
    private String id;
    private String fullName;
    private String mobilePhone;
    private String studentId;
    private String email;
    private int role;
    private String password;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String id, String fullName, String mobilePhone, String studentId, String email, int role) {
        this.id = id;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.studentId = studentId;
        this.email = email;
        this.role = role;
    }

    public User(String id, String fullName, String mobilePhone, String studentId, String email, int role, String password) {
        this.id = id;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.studentId = studentId;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


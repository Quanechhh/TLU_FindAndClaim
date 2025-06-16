package com.example.tlulostandclaim.data.model;

public class User {
    private String id;
    private String fullName;
    private String studentId;
    private String email;
    private String password;
    private String mobilePhone;
    private String role;

    // Constructor
    public User(String id, String fullName, String studentId, String email, String password, String mobilePhone, String role) {
        this.id = id;
        this.fullName = fullName;
        this.studentId = studentId;
        this.email = email;
        this.password = password;
        this.mobilePhone = mobilePhone;
        this.role = role;
    }

    public User() {
        // Constructor mặc định
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

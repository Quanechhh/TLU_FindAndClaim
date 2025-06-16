package com.example.tlulostandclaim.data.model;

public class User {
    private String id;
    private String fullName;
    private String mobilePhone;
    private String studentId;
    private String email;
    private String password;  // ← Thêm dòng này
    private String role;

    // Constructor rỗng
    public User() {
    }

    // Constructor đầy đủ (gồm password)
    public User(String id, String fullName, String mobilePhone, String studentId, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.studentId = studentId;
        this.email = email;
        this.role = role;
    }

    // Getter & Setter cho tất cả thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {  // ← Thêm getter
        return password;
    }

    public void setPassword(String password) {  // ← Thêm setter
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

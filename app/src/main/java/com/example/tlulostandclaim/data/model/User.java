package com.example.tlulostandclaim.data.model;

/**
 * Lớp User đại diện cho thông tin người dùng trong hệ thống.
 * Bao gồm các thông tin cá nhân, thông tin đăng nhập và phân quyền người dùng.
 */
public class User {
    // 🔹 Các thuộc tính (fields)
    private String id;             // ID của người dùng (tự sinh hoặc từ Firebase)
    private String fullName;       // Họ tên đầy đủ
    private String mobilePhone;    // Số điện thoại
    private String studentId;      // Mã số sinh viên
    private String email;          // Email người dùng (dùng để đăng nhập)
    private int role;              // Vai trò (ví dụ: 0 = user, 1 = admin)
    private String password;       // Mật khẩu (mã hóa hoặc plain text tuỳ yêu cầu)

    // 🔹 Constructor mặc định (dùng khi cần tạo object trống)
    public User() {}

    // 🔹 Constructor đăng nhập: chỉ cần email và password
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 🔹 Constructor tạo người dùng đầy đủ (chưa có password)
    public User(String id, String fullName, String mobilePhone, String studentId, String email, int role) {
        this.id = id;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.studentId = studentId;
        this.email = email;
        this.role = role;
    }

    // 🔹 Constructor đầy đủ nhất: dùng khi lấy từ DB hoặc tạo mới có đầy đủ thông tin
    public User(String id, String fullName, String mobilePhone, String studentId, String email, int role, String password) {
        this.id = id;
        this.fullName = fullName;
        this.mobilePhone = mobilePhone;
        this.studentId = studentId;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // 🔹 Getter và Setter cho từng thuộc tính

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

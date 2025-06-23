package com.example.tlulostandclaim.data.enum_model;

public enum EnumUserRole {
    ADMIN(0),

    EMPLOYEE(1),

    STUDENT(2);

    public final int value;

    EnumUserRole(int value) {
        this.value = value;
    }
}

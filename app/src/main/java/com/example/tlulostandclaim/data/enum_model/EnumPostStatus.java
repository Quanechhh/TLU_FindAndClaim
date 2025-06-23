package com.example.tlulostandclaim.data.enum_model;

public enum EnumPostStatus {
    WAITING(0),

    IN_STORAGE(1),

    DONE(2);

    public int value;

    EnumPostStatus(int value) {
        this.value = value;
    }
}

package com.example.tlulostandclaim.data.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestLostItemModel {
    private String id;
    private String userName;
    private String userPhone;
    private long createdAt;
    private String itemId;
    private String itemName;
    private List<String> itemImage;
    private int status; // 0 - process, 1 - decline , 2 - approve

    public RequestLostItemModel() {
    }

    public RequestLostItemModel(String id, String userName, String userPhone, long createdAt, String itemId, String itemName, List<String> itemImage, int status) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.createdAt = createdAt;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<String> getItemImage() {
        return itemImage;
    }

    public void setItemImage(List<String> itemImage) {
        this.itemImage = itemImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userName", userName);
        map.put("userPhone", userPhone);
        map.put("createdAt", createdAt);
        map.put("itemId", itemId);
        map.put("itemName", itemName);
        map.put("itemImage", itemImage);
        map.put("status", status);
        return map;
    }
}

package com.example.tlulostandclaim.data.model;

import java.util.HashMap;
import java.util.List;

public class LostItemModel {
    private String id;
    private String name;
    private String category;
    private String description;
    private List<String> image;
    private long createdAt;
    private String lostDate;
    private String lostLocation;
    private String contactInfo;
    private String userId;

    private int status;

    public LostItemModel() {
    }

    public LostItemModel(String id, String name, String category, List<String> image, long createdAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image = image;
        this.createdAt = createdAt;
    }

    public LostItemModel(String id, String name, String category, String description, List<String> image, long createdAt, String lostDate, String lostLocation, String contactInfo, String userId, int status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
        this.lostDate = lostDate;
        this.lostLocation = lostLocation;
        this.contactInfo = contactInfo;
        this.userId = userId;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getImage() {
        return image;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getLostLocation() {
        return lostLocation;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public String getLostDate() {
        return lostDate;
    }

    public HashMap<String, Object> convertModelToMap() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", id);
        hm.put("name", name);
        hm.put("category", category);
        hm.put("description", description);
        hm.put("image", image);
        hm.put("createdAt", createdAt);
        hm.put("lostLocation", lostLocation);
        hm.put("lostDate", lostDate);
        hm.put("contactInfo", contactInfo);
        return hm;
    }
}

package com.example.tlulostandclaim.data.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InStorageLostItemModel {
    private String id;
    private String title;
    private String description;
    private long createdDate;
    private List<String> images;
    private String type;
    private int status;

    public InStorageLostItemModel() {
    }

    public InStorageLostItemModel(String id, String title, String description, long createdDate, List<String> images, String type, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.images = images;
        this.type = type;
        this.status = status;
    }
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("description", description);
        map.put("createdDate", createdDate);
        map.put("images", images);
        map.put("type", type);
        map.put("status", status);
        return map;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

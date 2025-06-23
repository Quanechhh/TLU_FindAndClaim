package com.example.tlulostandclaim.data.model;

import java.util.HashMap;
import java.util.Map;

public class CategoryModel {
    private String name;

    public CategoryModel() {
    }

    public CategoryModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> convertToMap() {
        Map<String, String> hm = new HashMap<>();
        hm.put("name", name);
        return hm;
    }
}

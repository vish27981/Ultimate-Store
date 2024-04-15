package com.example.registration.model;

import androidx.annotation.NonNull;

public class CategoryModel {
    private String catId;
    private String catName;
    private String catImage;

    public CategoryModel(String catId, String catName, String catImage) {
        this.catId = catId;
        this.catName = catName;
        this.catImage = catImage;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    @Override
    @NonNull
    public String toString(){
        return "CategoryModel{"+
                "catId=" + catId + "," +
                "categoryName='" + catName + '\'' +"," +
                "categoryImage='" + catImage +'\''+
                '}';
    }
}

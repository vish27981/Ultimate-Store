package com.example.registration.model;


public class SubCategoryModel {

    private String subcatId;
    private String catId;
    private String subcategoryName;
    private String subcategoryImage;

    public SubCategoryModel(String subcatId,String catId, String subcategoryName, String subcategoryImage) {
        this.catId = catId;
        this.subcatId = subcatId;
        this.subcategoryName = subcategoryName;
        this.subcategoryImage = subcategoryImage;
    }

    public String getSubcatId() {
        return subcatId;
    }

    public void setSubcatId(String subcatId) {
        this.subcatId = subcatId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getSubcategoryImage() {
        return subcategoryImage;
    }

    public void setSubcategoryImage(String subcategoryImage) {
        this.subcategoryImage = subcategoryImage;
    }
}






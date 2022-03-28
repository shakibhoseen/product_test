package com.example.producttest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductModel implements Serializable {
    String name, description, id, imageUrl;
    List<ModelVariant> variantList;
    List<String> colorsBtnList, sizeBtnList;

    public ProductModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ModelVariant> getVariantList() {
        return variantList;
    }

    public void setVariantList(List<ModelVariant> variantList) {
        this.variantList = variantList;
    }

    public List<String> getColorsBtnList() {
        return colorsBtnList;
    }

    public void setColorsBtnList(List<String> colorsBtnList) {
        this.colorsBtnList = colorsBtnList;
    }

    public List<String> getSizeBtnList() {
        return sizeBtnList;
    }

    public void setSizeBtnList(List<String> sizeBtnList) {
        this.sizeBtnList = sizeBtnList;
    }
}

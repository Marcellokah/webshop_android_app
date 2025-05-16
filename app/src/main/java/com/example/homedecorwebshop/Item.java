// src/main/java/com/example/homedecorwebshop/Item.java
package com.example.homedecorwebshop;

public class Item {
    private String name;
    private int value; // Price in HUF
    private boolean inStock;
    private int imageResourceId; // To store the drawable resource ID for the image
    private String description;

    // Constructor
    public Item(String name, int value, boolean inStock, int imageResourceId, String description) {
        this.name = name;
        this.value = value;
        this.inStock = inStock;
        this.imageResourceId = imageResourceId;
        this.description = description;
    }

    // Default constructor required for calls to DataSnapshot.getValue(Item.class) if using Firebase Realtime Database/Firestore
    public Item() {
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public boolean isInStock() {
        return inStock;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    // Setter methods (optional, but can be useful)
    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
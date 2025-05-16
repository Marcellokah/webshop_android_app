// src/main/java/com/example/homedecorwebshop/Item.java
package com.example.homedecorwebshop;

import java.util.Objects;

public class Item {
    private String name;
    private int value; // Price in HUF
    private boolean inStock;
    private int imageResourceId;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name); // Assuming name is unique
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Assuming name is unique
    }

    public Item(String name, int value, boolean inStock, int imageResourceId, String description) {
        this.name = name;
        this.value = value;
        this.inStock = inStock;
        this.imageResourceId = imageResourceId;
        this.description = description;
    }

    // Default constructor
    public Item() {
    }

    // Getter methods
    public String getName() { return name; }
    public int getValue() { return value; }
    public boolean isInStock() { return inStock; }
    public int getImageResourceId() { return imageResourceId; }
    public String getDescription() { return description; }

    // Setter methods
    public void setName(String name) { this.name = name; }
    public void setValue(int value) { this.value = value; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public void setImageResourceId(int imageResourceId) { this.imageResourceId = imageResourceId; }
    public void setDescription(String description) { this.description = description; }
}
package com.example.homedecorwebshop;

import java.util.Objects;

/**
 * Represents an item available in the home decor webshop.
 * This class encapsulates details about an item such as its name, value (price),
 * stock status, image resource ID, and description.
 * <p>
 * Equality and hash code are determined solely by the item's {@code name}.
 * This means two {@code Item} objects are considered equal if they have the same name,
 * regardless of their other properties.
 * </p>
 */
public class Item {
    private String name;
    private int value; // Represents the price of the item
    private boolean inStock;
    private int imageResourceId; // Drawable resource ID for the item's image
    private String description;

    /**
     * Compares this item to the specified object. The result is {@code true} if and only if
     * the argument is not {@code null} and is an {@code Item} object that has the same
     * {@code name} as this object.
     *
     * @param o The object to compare this {@code Item} against.
     * @return {@code true} if the given object represents an {@code Item} equivalent to this item,
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name); // Equality based on name only
    }

    /**
     * Returns a hash code value for the item.
     * The hash code is based solely on the item's {@code name}.
     *
     * @return A hash code value for this item.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name); // Hash code based on name only
    }

    /**
     * Constructs a new {@code Item} with specified details.
     *
     * @param name            The name of the item.
     * @param value           The price of the item.
     * @param inStock         The stock availability of the item (true if in stock, false otherwise).
     * @param imageResourceId The Android drawable resource ID for the item's image.
     * @param description     A textual description of the item.
     */
    public Item(String name, int value, boolean inStock, int imageResourceId, String description) {
        this.name = name;
        this.value = value;
        this.inStock = inStock;
        this.imageResourceId = imageResourceId;
        this.description = description;
    }

    /**
     * Default constructor for an {@code Item}.
     * Creates an item with default (null or zero) values for its properties.
     * This constructor is often used by frameworks like Firebase for deserialization.
     */
    public Item() {
        // Default constructor
    }

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value (price) of the item.
     *
     * @return The price of the item.
     */
    public int getValue() {
        return value;
    }

    /**
     * Checks if the item is currently in stock.
     *
     * @return {@code true} if the item is in stock, {@code false} otherwise.
     */
    public boolean isInStock() {
        return inStock;
    }

    /**
     * Gets the Android drawable resource ID for the item's image.
     *
     * @return The resource ID for the image.
     */
    public int getImageResourceId() {
        return imageResourceId;
    }

    /**
     * Gets the description of the item.
     *
     * @return The textual description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the name of the item.
     *
     * @param name The new name for the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value (price) of the item.
     *
     * @param value The new price for the item.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Sets the stock availability of the item.
     *
     * @param inStock {@code true} if the item is in stock, {@code false} otherwise.
     */
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    /**
     * Sets the Android drawable resource ID for the item's image.
     *
     * @param imageResourceId The new resource ID for the image.
     */
    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    /**
     * Sets the description of the item.
     *
     * @param description The new textual description for the item.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
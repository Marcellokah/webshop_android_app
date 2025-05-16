// src/main/java/com/example/homedecorwebshop/CartManager.java
package com.example.homedecorwebshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {

    private static CartManager instance;
    private Map<Item, Integer> cartItems; // Item and its quantity

    private CartManager() {
        cartItems = new HashMap<>();
    }

    // Corrected getInstance() method
    public static synchronized CartManager getInstance() { // Return type added, extra 'static' removed
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(Item item) {
        if (cartItems.containsKey(item)) {
            cartItems.put(item, cartItems.get(item) + 1); // Increment quantity
        } else {
            cartItems.put(item, 1); // Add new item with quantity 1
        }
    }

    public void removeItem(Item item) {
        if (cartItems.containsKey(item)) {
            int quantity = cartItems.get(item);
            if (quantity > 1) {
                cartItems.put(item, quantity - 1);
            } else {
                cartItems.remove(item);
            }
        }
    }

    public Map<Item, Integer> getCartItems() {
        return new HashMap<>(cartItems); // Return a copy to prevent external modification
    }

    public List<Item> getItems() {
        return new ArrayList<>(cartItems.keySet());
    }

    // Corrected getQuantity() method for API level < 24
    public int getQuantity(Item item) {
        Integer quantity = cartItems.get(item);
        if (quantity == null) {
            return 0; // Default value if item is not in the map
        }
        return quantity;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public int getTotalItemCount() {
        int total = 0;
        for (int quantity : cartItems.values()) {
            total += quantity;
        }
        return total;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
            // Ensure getKey() and getValue() on the entry are not null if that's a possibility
            // though with how items are added, item should not be null.
            if (entry.getKey() != null) {
                totalPrice += entry.getKey().getValue() * entry.getValue();
            }
        }
        return totalPrice;
    }
}
package com.example.homedecorwebshop;

import java.util.HashMap;
import java.util.Map;

public class CartManager {

    private static CartManager instance;
    private final Map<Item, Integer> cartItems;

    private CartManager() {
        cartItems = new HashMap<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(Item item) {
        if (cartItems.containsKey(item)) {
            cartItems.put(item, cartItems.get(item) + 1);
        } else {
            cartItems.put(item, 1);
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
        return new HashMap<>(cartItems);
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
            if (entry.getKey() != null) {
                totalPrice += entry.getKey().getValue() * entry.getValue();
            }
        }
        return totalPrice;
    }
}
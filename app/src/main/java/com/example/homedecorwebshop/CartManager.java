package com.example.homedecorwebshop;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the shopping cart for the application using a Singleton pattern.
 * This class provides functionalities to add items, remove items, clear the cart,
 * retrieve all cart items, and calculate the total item count and total price.
 * <p>
 * The cart stores {@link Item} objects as keys and their respective quantities (Integer) as values
 * in a {@link HashMap}.
 * </p>
 * To get an instance of the cart manager, use {@link #getInstance()}.
 */
public class CartManager {

    private static CartManager instance;
    private final Map<Item, Integer> cartItems;

    /**
     * Private constructor to prevent direct instantiation and enforce the Singleton pattern.
     * Initializes the internal map for storing cart items.
     */
    private CartManager() {
        cartItems = new HashMap<>();
    }

    /**
     * Returns the singleton instance of {@code CartManager}.
     * This method is synchronized to ensure thread safety during the first-time instantiation.
     *
     * @return The single instance of {@code CartManager}.
     */
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    /**
     * Adds a given {@link Item} to the cart.
     * If the item already exists in the cart, its quantity is incremented by one.
     * If the item is not in the cart, it is added with a quantity of one.
     *
     * @param item The {@code Item} to be added to the cart. Must not be null.
     */
    public void addItem(Item item) {
        if (cartItems.containsKey(item)) {
            cartItems.put(item, cartItems.get(item) + 1);
        } else {
            cartItems.put(item, 1);
        }
    }

    /**
     * Removes a given {@link Item} from the cart.
     * If the item's quantity is greater than one, its quantity is decremented by one.
     * If the item's quantity is one, the item is completely removed from the cart.
     * If the item is not in the cart, this method does nothing.
     *
     * @param item The {@code Item} to be removed from the cart. Must not be null.
     */
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

    /**
     * Returns a defensive copy of the map containing all items in the cart and their quantities.
     * This prevents external modification of the internal cart state.
     *
     * @return A new {@code Map<Item, Integer>} representing the current items in the cart.
     * Returns an empty map if the cart is empty.
     */
    public Map<Item, Integer> getCartItems() {
        return new HashMap<>(cartItems); // Return a copy to prevent external modification
    }

    /**
     * Removes all items from the shopping cart, making it empty.
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Calculates and returns the total number of individual items in the cart.
     * This is the sum of quantities of all unique items.
     *
     * @return The total count of all items in the cart.
     */
    public int getTotalItemCount() {
        int total = 0;
        for (int quantity : cartItems.values()) {
            total += quantity;
        }
        return total;
    }

    /**
     * Calculates and returns the total price of all items currently in the cart.
     * This is calculated by summing the product of each item's price (value) and its quantity.
     * It safely handles cases where an {@link Item} key in the map might be null (though ideally,
     * items added should not be null and should have a valid price).
     *
     * @return The total price of all items in the cart as a {@code double}.
     */
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
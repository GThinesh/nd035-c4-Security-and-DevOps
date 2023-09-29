package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class DomainModelFactory {
    public static User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Admin");
        user.setPassword("admin123");

        Cart cart = createCart();
        user.setCart(cart);

        return user;
    }

    public static Cart createCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.ZERO);
        return cart;
    }

    public static UserOrder createOrder(User user) {
        return UserOrder.createFromCart(user.getCart());
    }


    public static Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Cake");
        item.setDescription("Delicious cake");
        item.setPrice(BigDecimal.valueOf(24.99));
        return item;
    }

    public static ModifyCartRequest newModifyRequest(String username, long itemId, int quantity) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
    }

}

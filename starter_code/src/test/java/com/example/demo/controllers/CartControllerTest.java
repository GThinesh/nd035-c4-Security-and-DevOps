package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.example.demo.controllers.DomainModelFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartControllerTest {
    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartRepository cartRepository;
    private User theUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        theUser = createUser();
        when(userRepository.findByUsername("admin")).thenReturn(theUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem()));
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());

    }

    @Test
    void addToCart_NoUser() {
        ResponseEntity<Cart> cart = cartController.addToCart(newModifyRequest("404", 1, 1));
        assertEquals(cart.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void removeFromCart_NoUser() {
        ResponseEntity<Cart> cart = cartController.removeFromCart(newModifyRequest("404", 1, 1));
        assertEquals(cart.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void addToCart_NoItem() {
        ResponseEntity<Cart> cart = cartController.addToCart(newModifyRequest("admin", 2, 1));
        assertEquals(cart.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void removeFromCart_NoItem() {
        ResponseEntity<Cart> cart = cartController.removeFromCart(newModifyRequest("admin", 2, 1));
        assertEquals(cart.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void addToCart_FiveCakes() {
        ResponseEntity<Cart> cart = cartController.addToCart(newModifyRequest("admin", 1, 5));
        verify(cartRepository).save(theUser.getCart());
        assertEquals(cart.getBody(), theUser.getCart());
    }

    @Test
    void removeFromCart_ThreeCakes() {
        cartController.addToCart(newModifyRequest("admin", 1, 5));
        ResponseEntity<Cart> cart = cartController.removeFromCart(newModifyRequest("admin", 1, 3));
        assertEquals(cart.getBody().getItems().size(), 2);
    }
}
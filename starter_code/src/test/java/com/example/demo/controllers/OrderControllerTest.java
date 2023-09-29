package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OrderControllerTest {
    private static User orderingUser;
    @InjectMocks
    private OrderController orderController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @BeforeAll
    static void beforeAll() {
        orderingUser = DomainModelFactory.createUser();
        orderingUser.getCart().addItem(DomainModelFactory.createItem());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findByUsername("admin")).thenReturn(orderingUser);
        when(orderRepository.findByUser(orderingUser)).thenReturn(Collections.singletonList(DomainModelFactory.createOrder(orderingUser)));
    }

    @Test
    void submit_forUser() {
        ResponseEntity<UserOrder> order = orderController.submit("admin");
        assertEquals(order.getStatusCode(), HttpStatus.OK);
        assertEquals(order.getBody().getItems().size(), 1);

    }

    @Test
    void submit_forNonExistingUser() {
        ResponseEntity<UserOrder> order = orderController.submit("admin1");
        assertEquals(order.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void getForUser() {
        ResponseEntity<List<UserOrder>> order = orderController.getOrdersForUser("admin");
        assertEquals(order.getStatusCode(), HttpStatus.OK);
        assertEquals(order.getBody().size(), 1);
        assertEquals(order.getBody().get(0).getItems().size(), 1);
    }

    @Test
    void getForNonExistingUser() {
        ResponseEntity<List<UserOrder>> order = orderController.getOrdersForUser("admin1");
        assertEquals(order.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.example.demo.controllers.DomainModelFactory.createUserRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.findById(1L)).thenReturn(Optional.of(DomainModelFactory.createUser()));
        when(userRepository.findByUsername("Admin")).thenReturn(DomainModelFactory.createUser());
        when(passwordEncoder.encode(any())).thenAnswer(arg -> arg.getArgument(0).toString() + "encoded");
    }

    @Test
    void findByUser_Existing() {
        ResponseEntity<User> user = userController.findByUserName("Admin");
        assertEquals(user.getStatusCode(), HttpStatus.OK);
        assertEquals(user.getBody().getUsername(), "Admin");

    }

    @Test
    void findByUser_NotExisting() {
        ResponseEntity<User> user = userController.findByUserName("admin1");
        assertEquals(user.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void findById_Existing() {
        ResponseEntity<User> user = userController.findById(1L);
        assertEquals(user.getStatusCode(), HttpStatus.OK);
        assertEquals(user.getBody().getUsername(), "Admin");

    }

    @Test
    void findById_NotExisting() {
        ResponseEntity<User> user = userController.findById(2L);
        assertEquals(user.getStatusCode(), HttpStatus.NOT_FOUND);

    }


    @Test
    void createUser() {
        ResponseEntity<User> user = userController.createUser(createUserRequest(10, true));
        assertEquals(user.getStatusCode(), HttpStatus.OK);

        User body = user.getBody();
        verify(cartRepository).save(body.getCart());
        verify(userRepository).save(body);
        assertTrue(body.getPassword().endsWith("encoded"));
    }


}
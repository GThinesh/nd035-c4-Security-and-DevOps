package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.demo.controllers.DomainModelFactory.createUserRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SareetaApplicationTests {
    @Autowired
    private UserController userController;

    public void contextLoads() {
    }

    @Test
    public void createUser_ShortPassword() {
        ResponseEntity<User> user = userController.createUser(createUserRequest(4, true));
        assertEquals(user.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_UnConfirmed() {
        ResponseEntity<User> user = userController.createUser(createUserRequest(10, false));
        assertEquals(user.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

}

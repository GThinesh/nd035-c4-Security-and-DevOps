package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controllers.DomainModelFactory.createItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem()));
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());
        when(itemRepository.findByName("Cake")).thenReturn(Collections.singletonList(createItem()));
        when(itemRepository.findByName("Bread")).thenReturn(Collections.emptyList());
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(createItem()));
    }

    @Test
    void getItems_GetTheOnlyOne() {
        ResponseEntity<List<Item>> items = itemController.getItems();
        assertEquals(items.getStatusCode(), HttpStatus.OK);
        assertEquals(items.getBody().size(), 1);

    }

    @Test
    void getById_Existing_ReturnsTheItem() {
        ResponseEntity<Item> itemById = itemController.getItemById(1L);
        assertEquals(itemById.getStatusCode(), HttpStatus.OK);
        assertEquals(itemById.getBody().getName(), "Cake");
    }

    @Test
    void getById_NonExisting_ReturnsTheItem() {
        ResponseEntity<Item> itemById = itemController.getItemById(2L);
        assertEquals(itemById.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void findByName_Existing_ReturnsTheItem() {
        ResponseEntity<List<Item>> byName = itemController.getItemsByName("Cake");
        assertEquals(byName.getStatusCode(), HttpStatus.OK);
        assertEquals(byName.getBody().size(), 1);
    }

    @Test
    void findByName_NonExisting_ReturnsTheItem() {
        ResponseEntity<List<Item>> byName = itemController.getItemsByName("Bread");
        assertEquals(byName.getStatusCode(), HttpStatus.NOT_FOUND);

    }
}
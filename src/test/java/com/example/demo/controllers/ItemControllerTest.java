package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ismailchaida.
 */
public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository;
    private Item item;

    @Before
    public void setUp() throws Exception {

        itemController = new ItemController();
        itemRepository = mock(ItemRepository.class);

        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        item = new Item();
        item.setId(1L);
        item.setName("Razer Blade");
        item.setPrice(BigDecimal.valueOf(3250.00));
    }


    @Test
    public void get_items() {
        Item itemSnd = new Item();
        itemSnd.setId(2L);
        itemSnd.setName("iPad Pro");
        itemSnd.setPrice(BigDecimal.valueOf(1750));

        List<Item> items = Arrays.asList(item, itemSnd);

        when(itemRepository.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemList = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(itemList);
    }

    @Test
    public void item_by_id() {
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemSnd = response.getBody();
        assertNotNull(itemSnd);
        assertEquals("Razer Blade", itemSnd.getName());
    }

    @Test
    public void item_by_id_failed() {
        long itemId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        final ResponseEntity<Item> response = itemController.getItemById(itemId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void item_by_name() {
        List<Item> items = Collections.singletonList(item);

        when(itemRepository.findByName("Razer Blade")).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Razer Blade");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals("Razer Blade", itemList.get(0).getName());
        assertEquals(1, itemList.size());
    }

    @Test
    public void item_by_name_failed() {
        String item = "Razer Blade";

        when(itemRepository.findByName(item)).thenReturn(Collections.emptyList());
        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}

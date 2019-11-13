package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.CreateItemRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void create_item_happy_path() throws Exception {
        CreateItemRequest req = new CreateItemRequest();
        req.setName("harry potter");
        req.setPrice(11.43);
        req.setDescription("hermione is african");

        ResponseEntity<Item> res = itemController.createItem(req);

        assertEquals(200, res.getStatusCodeValue());

        Item item = res.getBody();

        assertEquals("harry potter", item.getName());
    }

    @Test
    public void should_fail_price_less_than_zero() throws Exception {
        CreateItemRequest req = new CreateItemRequest();
        req.setName("harry potter");
        req.setPrice(-11.00);
        req.setDescription("hermione is african");

        ResponseEntity<Item> res = itemController.createItem(req);

        assertEquals(400, res.getStatusCodeValue());
    }

    @Test
    public void fetch_item_with_id_happy_path() throws Exception {

        Item item = new Item();
        item.setId(21L);
        item.setName("sugar");
        item.setDescription("sweet power");
        item.setPrice(new BigDecimal(22.99));

        when(itemRepository.findById(21L)).thenReturn(java.util.Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(21L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("sugar", response.getBody().getName());

    }

    @Test
    public void should_fail_fetch_item_with_id_does_not_exist() throws  Exception {

        final ResponseEntity<Item> response = itemController.getItemById(21L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void fetch_item_with_name_happy_path() throws Exception {

        Item item1 = new Item();
        item1.setId(21L);
        item1.setName("sugar");
        item1.setDescription("sweet power");
        item1.setPrice(new BigDecimal(22.99));

        Item item2 = new Item();
        item2.setId(22L);
        item2.setName("sugar");
        item2.setDescription("sweet power");
        item2.setPrice(new BigDecimal(22.99));

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        when(itemRepository.findByName("sugar")).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("sugar");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

    }

    @Test
    public void fetch_all_items_happy_path() throws Exception {

        Item item1 = new Item();
        item1.setId(21L);
        item1.setName("sugar");
        item1.setDescription("sweet power");
        item1.setPrice(new BigDecimal(22.99));

        Item item2 = new Item();
        item2.setId(22L);
        item2.setName("sugar");
        item2.setDescription("sweet power");
        item2.setPrice(new BigDecimal(22.99));

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        when(itemRepository.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());

    }

}

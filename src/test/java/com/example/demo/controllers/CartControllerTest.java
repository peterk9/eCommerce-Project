package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }


    @Test
    public void add_to_cart_happy_path(){
        User u = new User();
        u.setId(99L);
        u.setUsername("peterk9");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("sugar");
        item1.setDescription("sweet power");
        item1.setPrice(new BigDecimal(2.00));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("juice");
        item2.setDescription("orange juice");
        item2.setPrice(new BigDecimal(1.00));

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Cart c = new Cart();
        c.setId(88L);
        c.setItems(items);

        u.setCart(c);

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(3);
        r.setUsername("peterk9");

        when(userRepository.findByUsername(r.getUsername())).thenReturn(u);
        when(itemRepository.findById(r.getItemId())).thenReturn(java.util.Optional.of(item1));

        final ResponseEntity<Cart> res = cartController.addTocart(r);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals(5, res.getBody().getItems().size());
        assertEquals(new BigDecimal(6), res.getBody().getTotal());
    }

    @Test
    public void should_fail_user_not_found(){

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(3);
        r.setUsername("peterk9");

        final ResponseEntity<Cart> res = cartController.addTocart(r);

        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void should_fail_item_not_found(){
        User u = new User();
        u.setId(99L);
        u.setUsername("peterk9");

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(3);
        r.setUsername("peterk9");

        when(userRepository.findByUsername(r.getUsername())).thenReturn(u);

        final ResponseEntity<Cart> res = cartController.addTocart(r);

        assertEquals(404, res.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart_happy_path(){
        User u = new User();
        u.setId(99L);
        u.setUsername("peterk9");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("sugar");
        item1.setDescription("sweet power");
        item1.setPrice(new BigDecimal(2.00));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("juice");
        item2.setDescription("orange juice");
        item2.setPrice(new BigDecimal(1.00));

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Cart c = new Cart();
        c.setId(88L);
        c.setItems(items);

        u.setCart(c);

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("peterk9");

        when(userRepository.findByUsername(r.getUsername())).thenReturn(u);
        when(itemRepository.findById(r.getItemId())).thenReturn(java.util.Optional.of(item1));

        final ResponseEntity<Cart> res = cartController.removeFromcart(r);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals(1, res.getBody().getItems().size());
    }

}

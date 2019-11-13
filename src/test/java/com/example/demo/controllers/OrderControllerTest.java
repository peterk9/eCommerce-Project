package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    // submit order
    @Test
    public void submit_order_happy_path(){

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

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);

        ResponseEntity<UserOrder> res =  orderController.submit(u.getUsername());

        assertEquals(200, res.getStatusCodeValue());

        UserOrder order = res.getBody();

        assertEquals(u.getCart().getItems().size(), order.getItems().size());
        assertEquals(u.getCart().getTotal(), order.getTotal());
        assertEquals(u.getCart().getUser(), order.getUser());

    }

    @Test
    public void should_fail_user_does_not_exit(){
        ResponseEntity<UserOrder> res =  orderController.submit("peterk9");

        assertEquals(404, res.getStatusCodeValue());
    }

    // fetch order history
    @Test
    public void fetch_order_history_happy_path(){

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

        UserOrder order = new UserOrder();
        order.setUser(u);
        order.setItems(items);
        order.setTotal(c.getTotal());

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);
        when(orderRepository.findByUser(u)).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> res =  orderController.getOrdersForUser(u.getUsername());

        assertEquals(200, res.getStatusCodeValue());
        assertEquals(1, res.getBody().size());

    }



}

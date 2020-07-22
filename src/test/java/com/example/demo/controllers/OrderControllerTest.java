package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ismailchaida.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private User user;
    private Item item;
    private Cart cart;

    @Before
    public void setUp() {
        orderController = new OrderController();
        userRepository = mock(UserRepository.class);
        orderRepository = mock(OrderRepository.class);

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        user = new User();
        user.setId(1L);
        user.setUsername("ichaida");
        user.setPassword("password");

        item = new Item();
        item.setId(1L);
        item.setName("Razer Blade");
        item.setPrice(BigDecimal.valueOf(3250.00));

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(BigDecimal.valueOf(3250.00));

        user.setCart(cart);
    }

    @Test
    public void submit() {
        when(userRepository.findByUsername("ichaida")).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit("ichaida");
        UserOrder userOrder = response.getBody();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(userOrder);
        assertEquals(user, userOrder.getUser());
    }

    @Test
    public void submit_failed() {
        when(userRepository.findByUsername("ichaida")).thenReturn(null);
        final ResponseEntity<UserOrder> response = orderController.submit("ichaida");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void order_by_user() {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);

        when(userRepository.findByUsername("ichaida"))
            .thenReturn(user);
        when(orderRepository.findByUser(user))
            .thenReturn(Collections.singletonList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ichaida");
        List<UserOrder> userOrders = response.getBody();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(userOrders);
        assertEquals(user, userOrders.get(0).getUser());
    }

    @Test
    public void order_by_user_failed() {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);

        when(userRepository.findByUsername("ichaida"))
            .thenReturn(null);
        when(orderRepository.findByUser(user))
            .thenReturn(Collections.singletonList(userOrder));

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ichaida");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}

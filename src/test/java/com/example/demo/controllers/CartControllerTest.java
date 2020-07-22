package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author ismailchaida.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private CartRepository cartRepository;

    @Before
    public void setUp() {
        cartController = new CartController();
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        cartRepository = mock(CartRepository.class);

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
    }

    @Test
    public void add_to_cart() {
        // Prepare an item
        Item item = new Item();
        item.setId(1L);
        item.setName("Razer Blade");
        item.setPrice(BigDecimal.valueOf(3250.00));
        // Mock the item
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Prepare an user
        User userFst = new User();
        userFst.setId(1L);
        userFst.setUsername("ichaida");
        userFst.setPassword("password");
        // Mock the user
        when(userRepository.findByUsername("ichaida")).thenReturn(userFst);

        // Attach a cart
        Cart cartFst = new Cart();
        cartFst.setUser(userFst);
        userFst.setCart(cartFst);
        when(cartRepository.findByUser(userFst)).thenReturn(cartFst);

        // Prepare a request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("ichaida");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Cart cartSnd = response.getBody();
        assert cartSnd != null;
        User userSnd = cartSnd.getUser();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(cartSnd);
        assertNotNull(userSnd);
        assertEquals(userFst, cartFst.getUser());
        assertEquals(item, cartFst.getItems().get(0));
    }

    @Test
    public void add_to_cart_cart_failed() {
        // Prepare an user
        User userFst = new User();
        userFst.setId(2L);
        userFst.setUsername("ichaida");
        userFst.setPassword("password");

        // Prepare an item
        Item item = new Item();
        item.setId(2L);
        item.setName("Razer Blade");
        item.setPrice(BigDecimal.valueOf(3250.00));

        // Attach a cart
        Cart cartFst = new Cart();
        cartFst.setUser(userFst);
        userFst.setCart(cartFst);
        when(cartRepository.findByUser(userFst)).thenReturn(cartFst);

        when(userRepository.findByUsername("ichaida")).thenReturn(null);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        // Prepare a request
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("ichaida");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart(){
        User user = new User();
        user.setId(1L);
        user.setUsername("ichaida");
        user.setPassword("password");

        Item item = new Item();
        item.setId(1L);
        item.setName("Razer Blade");
        item.setPrice(BigDecimal.valueOf(3250.00));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("ichaida")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(cartRepository.findByUser(user)).thenReturn(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("ichaida");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart cartSnd = response.getBody();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(cartSnd);
        assertEquals(0, cartSnd.getItems().size());
    }

    @Test
    public void remove_from_cart_failed() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ichaida");
        user.setPassword("password");

        Item item = new Item();
        item.setId(1L);
        item.setName("Razer Blade");
        item.setPrice(BigDecimal.valueOf(3250.00));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("ichaida"))
            .thenReturn(null);
        when(itemRepository.findById(1L))
            .thenReturn(Optional.of(item));
        when(cartRepository.findByUser(user))
            .thenReturn(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("ichaida");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}

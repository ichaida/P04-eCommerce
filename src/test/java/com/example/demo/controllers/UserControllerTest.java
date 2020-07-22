package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

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
public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() throws Exception {

        userController = new UserController();
        userRepository = mock(UserRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user() {

        String username = "ichaida";
        String password = "password";

        when(bCryptPasswordEncoder.encode(password))
            .thenReturn("password_hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("ichaida", user.getUsername());
        assertEquals("password_hashed", user.getPassword());
    }

    @Test
    public void create_user_failed() {

        String username = "ichaida";
        String password = "";

        when(bCryptPasswordEncoder.encode(password))
            .thenReturn("password_hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name() {

        String username = "ichaida";
        String password = "password";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("password_hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        when(userRepository.findByUsername(username))
            .thenReturn(userResponseEntity.getBody());

        final ResponseEntity<User> response = userController.findByUserName(username);
        User user = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(user);
    }

    @Test
    public void find_user_by_name_failed() {

        String username = "ichaida";
        String password = "password";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("password_hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(null);

        final ResponseEntity<User> response = userController.findByUserName(username);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void find_by_id() {
        String username = "ichaida";
        String password = "password";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("password_hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        final ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);

        when(userRepository.findById(0L)).thenReturn(Optional.ofNullable(userResponseEntity.getBody()));

        final ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(userResponseEntity.getBody().getId(), user.getId());
    }

    @Test
    public void find_by_id_failed() {
        String username = "ichaida";
        String password = "password";

        when(bCryptPasswordEncoder.encode(password)).thenReturn("password_hashed");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword(password);

        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        final ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}

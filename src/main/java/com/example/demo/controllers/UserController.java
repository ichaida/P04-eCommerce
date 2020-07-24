package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author ismailchaida
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            log.info("findUserById | User Id: {} was retrieved successfully", id);
            return ResponseEntity.of(userOpt);
        }else {
            log.error("findUserById | User Id: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("findByUserName | No user found matching {}", username);
            return ResponseEntity.notFound().build();
        }
        log.debug("findByUserName | Fetching the user {}", username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if ((createUserRequest.getPassword().length() < 7) || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            log.error("createUser | Error with user password. Cannot create user {}", createUserRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        log.debug("createUser | User {} created successfully", createUserRequest.getUsername());
        return ResponseEntity.ok(user);
    }

}

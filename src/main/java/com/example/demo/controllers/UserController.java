package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.security.SecureRandom;
import java.util.Random;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		String username = createUserRequest.getUsername();
		String password = createUserRequest.getPassword();
		String confirmPassword = createUserRequest.getConfirmPassword();

		if(!password.equals(confirmPassword) || password.length() < 7){
			// TODO: log
			return ResponseEntity.badRequest().build();
		}

		Random r = new SecureRandom();
		byte[] salt = new byte[20];
		r.nextBytes(salt);

		String saltString = salt.toString();

		String passwordHash = bCryptPasswordEncoder.encode(password + saltString);

		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordHash);
		user.setSalt(saltString);

		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		userRepository.save(user);

		return ResponseEntity.ok(user);
	}
	
}

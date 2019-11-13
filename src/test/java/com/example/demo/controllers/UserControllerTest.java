package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("testUsername");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("testUsername", u.getUsername());
        assertNotNull(u.getSalt());
    }

    @Test
    public void should_fail_if_password_does_not_match_confirm_password() throws Exception {

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user");
        r.setPassword("234234");
        r.setConfirmPassword("123123");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void should_fail_if_password_length_to_small() throws Exception {

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("user");
        r.setPassword("123");
        r.setConfirmPassword("123");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void fetch_user_with_username_happy_path() throws Exception {

        User u = new User();
        u.setId(0);
        u.setUsername("kevin");
        u.setPassword("halo123");

        when(userRepository.findByUsername("kevin")).thenReturn(u);

        final ResponseEntity<User> response = userController.findByUserName("kevin");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void should_fail_fetching_user_does_not_exist() throws Exception {

        User u = new User();
        u.setId(0);
        u.setUsername("ashley");
        u.setPassword("halo123");

        final ResponseEntity<User> response = userController.findByUserName("ashley");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void fetch_user_with_id_happy_path() throws Exception {

        User u = new User();
        u.setId(321312L);
        u.setUsername("kevin");
        u.setPassword("halo123");

        when(userRepository.findById(321312L)).thenReturn(java.util.Optional.of(u));

        final ResponseEntity<User> response = userController.findById(321312L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void should_fail_fetch_user_with_id_does_not_exist() throws Exception {

        final ResponseEntity<User> response = userController.findById(123123L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

}

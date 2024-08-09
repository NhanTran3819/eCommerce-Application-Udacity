package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	private UserController userController;

	private UserRepository userRepository = mock(UserRepository.class);

	private CartRepository cartRepository = mock(CartRepository.class);

	private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

	@Before
	public void setUp(){
		this.userController = new UserController();
		TestUtils.injectObjects(userController, "userRepository", userRepository);
		TestUtils.injectObjects(userController, "cartRepository", cartRepository);
		TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
	}

	@Test
	public void testGetItemById() {
		User user = TestUtils.getUser();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		ResponseEntity<User> out = userController.findById(1L);
		assertEquals(200, out.getStatusCodeValue());
		assertEquals(user.getId(), out.getBody().getId());
		assertEquals(user.getUsername(), out.getBody().getUsername());
		assertEquals(user.getPassword(), out.getBody().getPassword());
		assertEquals(user.getCart(), out.getBody().getCart());
	}

	@Test
	public void testGetItemsByName() {
		User user = TestUtils.getUser();
		when(userRepository.findByUsername("userTest")).thenReturn(user);
		ResponseEntity<User> out = userController.findByUserName("userTest");
		assertEquals(200, out.getStatusCodeValue());
		assertEquals(user.getId(), out.getBody().getId());
		assertEquals(user.getUsername(), out.getBody().getUsername());
		assertEquals(user.getPassword(), out.getBody().getPassword());
		assertEquals(user.getCart(), out.getBody().getCart());

		when(userRepository.findByUsername("userTest")).thenReturn(null);
		out = userController.findByUserName("userTest");
		assertEquals(ResponseEntity.notFound().build(), out);
	}

	@Test
	public void testCreateUser() {
		CreateUserRequest userRequest = new CreateUserRequest();
		userRequest.setUsername("testUser");
		userRequest.setPassword("testPassword");
		userRequest.setConfirmPassword("testPassword");
		User user = new User();
		user.setUsername("testUser");
		user.setCart(new Cart());
		user.setPassword("bCryptPasswordEncoder");
		when(bCryptPasswordEncoder.encode(any())).thenReturn("bCryptPasswordEncoder");

		ResponseEntity<User> out = userController.createUser(userRequest);
		assertEquals(200, out.getStatusCodeValue());
		assertEquals(user.getUsername(), out.getBody().getUsername());
		assertEquals(user.getPassword(), out.getBody().getPassword());
		assertNull(out.getBody().getCart().getId());
		assertNull(out.getBody().getCart().getUser());
		assertNull(out.getBody().getCart().getTotal());
		assertNull(out.getBody().getCart().getItems());
		verify(cartRepository, times(1)).save(any());
		verify(userRepository, times(1)).save(any());

		userRequest.setPassword("1234567");
		out = userController.createUser(userRequest);
		assertEquals(ResponseEntity.badRequest().build(), out);

		userRequest.setPassword("12345678");
		userRequest.setConfirmPassword("1234567890");
		out = userController.createUser(userRequest);
		assertEquals(ResponseEntity.badRequest().build(), out);
	}
}
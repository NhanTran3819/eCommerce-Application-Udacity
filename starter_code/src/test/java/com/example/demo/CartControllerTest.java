package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
	@InjectMocks
	private CartController cartController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ItemRepository itemRepository;

	@Before
	public void setup(){
	}

	@Test
	public void testAddTocart() {
		ModifyCartRequest value = TestUtils.getModifyCartRequest();
		User user = TestUtils.getUser();
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.of(TestUtils.getItem()));
		ResponseEntity<Cart> out = cartController.addTocart(value);
		assertEquals(ResponseEntity.ok(user.getCart()), out);
		verify(cartRepository, times(1)).save(any());

		when(userRepository.findByUsername(any())).thenReturn(null);
		out = cartController.addTocart(value);
		assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), out);

		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.empty());
		out = cartController.addTocart(value);
		assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), out);
	}

	@Test
	public void testRemoveFromcart() {
		ModifyCartRequest value = TestUtils.getModifyCartRequest();
		User user = TestUtils.getUser();
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.of(TestUtils.getItem()));
		ResponseEntity<Cart> out = cartController.removeFromcart(value);
		assertEquals(ResponseEntity.ok(user.getCart()), out);
		verify(cartRepository, times(1)).save(any());

		when(userRepository.findByUsername(any())).thenReturn(null);
		out = cartController.removeFromcart(value);
		assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), out);

		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.empty());
		out = cartController.removeFromcart(value);
		assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), out);
	}
}
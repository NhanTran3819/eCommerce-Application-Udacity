package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
	@InjectMocks
	private OrderController orderController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private OrderRepository orderRepository;

	@Before
	public void setup(){
	}

	@Test
	public void testSubmit() {
		User user = TestUtils.getUser();
		UserOrder order = UserOrder.createFromCart(user.getCart());
		when(userRepository.findByUsername(any())).thenReturn(user);
		ResponseEntity<UserOrder> out = orderController.submit("testUser");

		assertEquals(200, out.getStatusCodeValue());
		assertEquals(order.getId(), out.getBody().getId());
		assertEquals(order.getItems(), out.getBody().getItems());
		assertEquals(order.getTotal(), out.getBody().getTotal());
		assertEquals(order.getUser(), out.getBody().getUser());
		verify(orderRepository, times(1)).save(any());

		when(userRepository.findByUsername(any())).thenReturn(null);
		out = orderController.submit("testUser");
		assertEquals(ResponseEntity.notFound().build(), out);
	}

	@Test
	public void testGetOrdersForUser() {
		User user = TestUtils.getUser();
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(orderRepository.findByUser(any())).thenReturn(new ArrayList<>());
		ResponseEntity<List<UserOrder>> out = orderController.getOrdersForUser("testUser");
		assertEquals(ResponseEntity.ok(new ArrayList()), out);

		when(userRepository.findByUsername(any())).thenReturn(null);
		out = orderController.getOrdersForUser("testUser");
		assertEquals(ResponseEntity.notFound().build(), out);
	}
}
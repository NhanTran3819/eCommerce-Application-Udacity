package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
	@InjectMocks
	private ItemController itemController;
	@Mock
	private ItemRepository itemRepository;

	@Before
	public void setup(){
	}

	@Test
	public void testGetItems() {
		List<Item> items = new ArrayList<>();
		items.add(TestUtils.getItem());
		when(itemRepository.findAll()).thenReturn(items);
		ResponseEntity<List<Item>> out = itemController.getItems();
		assertEquals(ResponseEntity.ok(items), out);
	}

	@Test
	public void testGetItemById() {
		Optional<Item> optionalItem = Optional.of(TestUtils.getItem());
		when(itemRepository.findById(1L)).thenReturn(optionalItem);
		ResponseEntity<Item> out = itemController.getItemById(1L);
		assertEquals(ResponseEntity.ok(TestUtils.getItem()), out);
	}

	@Test
	public void testGetItemsByName() {
		List<Item> items = new ArrayList<>();
		items.add(TestUtils.getItem());
		when(itemRepository.findByName("userTest")).thenReturn(items);
		ResponseEntity<List<Item>> out = itemController.getItemsByName("userTest");
		assertEquals(ResponseEntity.ok(items), out);

		when(itemRepository.findByName("userTest")).thenReturn(null);
		out = itemController.getItemsByName("userTest");
		assertEquals(ResponseEntity.notFound().build(), out);

		when(itemRepository.findByName("userTest")).thenReturn(new ArrayList<>());
		out = itemController.getItemsByName("userTest");
		assertEquals(ResponseEntity.notFound().build(), out);
	}
}
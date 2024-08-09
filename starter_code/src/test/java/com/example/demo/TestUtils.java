package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

	public static User getUser() {
		User user = new User();
		user.setId(1L);
		user.setUsername("testUser");
		user.setPassword("12345678");
		user.setCart(getCart());
		return user;
	}

	public static Cart getCart() {
		Cart cart = new Cart();
		cart.setId(1L);
		List<Item> items = new ArrayList<>();
		items.add(getItem());
		cart.setItems(items);
		cart.setTotal(BigDecimal.valueOf(2));
		cart.setUser(new User());
		return cart;
	}

	public static Item getItem() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Item name");
		item.setPrice(BigDecimal.valueOf(10000));
		item.setDescription("Description of item");
		return item;
	}

	public static ModifyCartRequest getModifyCartRequest() {
		ModifyCartRequest value = new ModifyCartRequest();
		value.setItemId(1L);
		value.setUsername("userTest");
		value.setQuantity(1);
		return value;
	}

	public static void injectObjects(Object target, String fieldName, Object toInject) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, toInject);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}

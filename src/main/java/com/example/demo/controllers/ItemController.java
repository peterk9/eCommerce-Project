package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.model.requests.CreateItemRequest;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		return items == null || items.isEmpty() ?
				ResponseEntity.notFound().build() :
				ResponseEntity.ok(items);
	}

	@PostMapping("/create")
	public ResponseEntity<Item> createItem(@RequestBody CreateItemRequest createItemRequest){
		String name = createItemRequest.getName();
		double price = createItemRequest.getPrice();
		String description = createItemRequest.getDescription();

		if (price < 0) {
			log.error("price cannot be less than 0");
			return ResponseEntity.badRequest().build();
		}

		Item item = new Item();

		item.setName(name);
		item.setPrice(new BigDecimal(price));
		item.setDescription(description);

		itemRepository.save(item);

		log.info("new item created :", item.getId());

		return ResponseEntity.ok(item);
	}
	
}

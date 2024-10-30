package org.mrmachine.springcloud.msvc_users.controllers;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mrmachine.springcloud.msvc_users.entities.User;
import org.mrmachine.springcloud.msvc_users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
	
	private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

	@GetMapping
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(this.service.findAll());
	}
	
	@GetMapping("/users-by-ids-course")
	public ResponseEntity<?> findAllById(@RequestParam List<Long> ids) {
		return ResponseEntity.ok(this.service.findAllById(ids));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Optional<User> userOpt = this.service.findById(id);
		if (userOpt.isPresent()) {
			return ResponseEntity.ok(userOpt.get());
		} else {			
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			return valid(result);
		} else if ( !user.getEmail().isEmpty() && this.service.existsByEmail(user.getEmail()) ) {
			return ResponseEntity
					.badRequest()
					.body(Collections.singletonMap("message", "The field email has already been setted with that value!."));
		} else {
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(this.service.save(user).get());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result) {
		
		
		if (result.hasErrors()) {
			return valid(result);
		}
		
		Optional<User> userOpt = this.service.findById(id);
		
		if (userOpt.isPresent()) {
			
			User currentUser = userOpt.get();
			
			if ( !user.getEmail().equalsIgnoreCase(currentUser.getEmail()) && !user.getEmail().isEmpty() && this.service.existsByEmail(user.getEmail()) ) {
				return ResponseEntity
						.badRequest()
						.body(Collections.singletonMap("message", "The field email has already been setted with that value!."));
			}
			
			currentUser.setName(user.getName());
			currentUser.setEmail(user.getEmail());
			currentUser.setPassword(user.getPassword());
			
			
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body(this.service.save(currentUser).get());
			
		} else {
			return ResponseEntity
					.notFound()
					.build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Optional<User> userOpt = this.service.findById(id);
		if (userOpt.isPresent()) {
			this.service.delete(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	private ResponseEntity<?> valid(BindingResult result) {
		
		Map<String, String> errors = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errors.put(err.getField(), "The field " + err.getField() +" "+ err.getDefaultMessage()+"!");
		});
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(errors);
	}
}

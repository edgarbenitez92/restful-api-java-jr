package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {

	private UserDaoService userService;

	public UserResource(UserDaoService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/user/{id}")
	public User retrieveUserById(@PathVariable int id) {
		User user = userService.getUserById(id);

		if (user == null) {
			throw new UserNotFoundException(String.format("Usuario con id %s no existe", id));
		}

		return user;
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User createdUser = userService.saveUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/{id}")
				.buildAndExpand(createdUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/user/{id}")
	public void deleteUserById(@PathVariable int id) {
		userService.deleteUserById(id);
	}
}

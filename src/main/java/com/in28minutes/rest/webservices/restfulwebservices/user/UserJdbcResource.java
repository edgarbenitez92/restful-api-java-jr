package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.jdbc.UserJdbcRepository;

import jakarta.validation.Valid;

@RestController
public class UserJdbcResource {

	private UserDaoService userService;
	private UserJdbcRepository userRepository;

	public UserJdbcResource(UserDaoService userService, UserJdbcRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@GetMapping("/jdbc/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAllUsers();
	}

	@GetMapping("/jdbc/user/{id}")
	public EntityModel<User> retrieveUserById(@PathVariable int id) {
		User user;

		try {
			user = userRepository.findUserById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new UserNotFoundException(String.format("Usuario con id %s no existe", id));
		}

		EntityModel<User> entityModel = EntityModel.of(user);

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));

		return entityModel;
	}

	@PostMapping("/jdbc/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User createdUser = userRepository.createUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/{id}")
				.buildAndExpand(createdUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/jdbc/user/{id}")
	public void deleteUserById(@PathVariable int id) {
		userRepository.deleteUserById(id);
	}
}

package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserJpaRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	private UserJpaRepository userRepository;

	public UserJpaResource(UserJpaRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/jpa/user/{id}")
	public EntityModel<User> retrieveUserById(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("Usuario con id %s no existe", id));
		}

		EntityModel<User> entityModel = EntityModel.of(user.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));

		return entityModel;
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User createdUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/{id}")
				.buildAndExpand(createdUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/jpa/user/{id}")
	public void deleteUserById(@PathVariable int id) {
		userRepository.deleteById(id);
	}
	
	@GetMapping("/jpa/user/{id}/posts")
	public List<PostUser> retrievePostsForUser(@PathVariable int id) {

		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("Usuario con id %s no existe", id));
		}
		
		return user.get().getPostsUser();
	}
}

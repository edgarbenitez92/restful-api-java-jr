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

import com.in28minutes.rest.webservices.restfulwebservices.jdbc.PostJdbcRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jdbc.UserJdbcRepository;

import jakarta.validation.Valid;

@RestController
public class UserJdbcResource {
	private UserJdbcRepository userRepository;
	private PostJdbcRepository postJdbcRepository;

	public UserJdbcResource(UserJdbcRepository userRepository, PostJdbcRepository postJdbcRepository) {
		this.userRepository = userRepository;
		this.postJdbcRepository = postJdbcRepository;
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

	@GetMapping("/jdbc/user/{id}/posts")
	public List<PostUser> retrievePostsForUser(@PathVariable int id) {
		User user = userRepository.findUserWithPostsById(id);
		return user.getPostsUser();
	}

	@PostMapping("/jdbc/user/{id}/posts")
	public ResponseEntity<PostUser> createPostUser(@PathVariable int id, @Valid @RequestBody PostUser postUser) {
		PostUser user = postJdbcRepository.createPostUser(postUser);

		if (user == null) {
			throw new UserNotFoundException(String.format("Usuario con id %s no existe", id));
		}

		PostUser createdPostUser = postJdbcRepository.createPostUser(postUser);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/{id}")
				.buildAndExpand(createdPostUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}

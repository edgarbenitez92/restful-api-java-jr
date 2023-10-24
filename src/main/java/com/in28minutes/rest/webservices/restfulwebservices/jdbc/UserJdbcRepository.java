package com.in28minutes.rest.webservices.restfulwebservices.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.in28minutes.rest.webservices.restfulwebservices.user.PostUser;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserNotFoundException;

@Repository
public class UserJdbcRepository {

	@Autowired
	private JdbcTemplate springJdbcTemplate;

	private static String FIND_ALL_USERS = """
			select * from user_details
			""";

	public List<User> findAllUsers() {
		return springJdbcTemplate.query(FIND_ALL_USERS, new BeanPropertyRowMapper<>(User.class));
	}

	private static String FIND_USER_BY_ID = """
			select * from user_details where id=?
			""";

	public User findUserById(long id) {
		return springJdbcTemplate.queryForObject(FIND_USER_BY_ID, new BeanPropertyRowMapper<>(User.class), id);
	}

	private static String DELETE_USER_BY_ID = """
			delete from user_details where id=?
			""";

	public void deleteUserById(long id) {
		springJdbcTemplate.update(DELETE_USER_BY_ID, id);
	}

	private static String CREATE_USER = """
			insert into course (user_name, birthDate)
			values(?, ?, ?);
			""";

	public User createUser(User user) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		springJdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getName());
			ps.setDate(2, Date.valueOf(user.getBirthDate()));
			return ps;
		}, keyHolder);

		long generatedId = keyHolder.getKey().longValue();

		return findUserById(generatedId);
	}

	private static String FIND_POSTS_BY_USER_ID = """
			SELECT p.id, p.description
			FROM post p
			WHERE p.user_id = ?;
			""";

	public List<PostUser> findPostsByUserId(long userId) {
		return springJdbcTemplate.query(FIND_POSTS_BY_USER_ID, new BeanPropertyRowMapper<>(PostUser.class), userId);
	}

	public User findUserWithPostsById(long userId) {
		User user = findUserById(userId);

		if (user == null) {
			throw new UserNotFoundException(String.format("Usuario con id %s no existe", userId));
		}

		List<PostUser> posts = findPostsByUserId(userId);
		user.setPostsUser(posts);

		return user;
	}
}

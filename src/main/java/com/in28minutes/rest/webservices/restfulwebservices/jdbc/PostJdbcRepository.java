package com.in28minutes.rest.webservices.restfulwebservices.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.in28minutes.rest.webservices.restfulwebservices.user.PostUser;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import com.in28minutes.rest.webservices.restfulwebservices.user.UserNotFoundException;

@Repository
public class PostJdbcRepository {

	@Autowired
	private JdbcTemplate springJdbcTemplate;

	private static String FIND_POST_USER_BY_ID = """
			select * from post where id=?
			""";

	public PostUser findUserById(long id) {
		return springJdbcTemplate.queryForObject(FIND_POST_USER_BY_ID, new BeanPropertyRowMapper<>(PostUser.class), id);
	}

	private static String CREATE_POST_USER = """
			insert into post (description,user_id,id)
			values(?, ?, default);
			""";

	public PostUser createPostUser(PostUser postUser) {
		int userId = postUser.getUser().getId();

		String selectUserDetailsQuery = "select u1_0.id, u1_0.birth_date, u1_0.name from user_details u1_0 where u1_0.id = ?";
		String selectNextPostIdQuery = "select next value for post_seq";
		String insertPostQuery = "insert into post (description, user_id, id) values (?, ?, ?)";

		int postUserId = -1;

		User user = springJdbcTemplate.queryForObject(selectUserDetailsQuery, new BeanPropertyRowMapper<>(User.class),
				userId);

		if (user != null) {
			int nextPostId = springJdbcTemplate.queryForObject(selectNextPostIdQuery, Integer.class);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			springJdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(insertPostQuery, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, postUser.getDescription());
				ps.setInt(2, userId);
				ps.setInt(3, nextPostId);
				return ps;
			}, keyHolder);

			postUserId = keyHolder.getKey().intValue();
		}

		PostUser createdPostUser = new PostUser();
		createdPostUser.setId(postUserId);
		createdPostUser.setDescription(postUser.getDescription());

		return createdPostUser;
	}
}

package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<>();
	private static int usersCount = 0;
	
	static {
		users.add(new User(++usersCount, "Edgar", LocalDate.now().minusYears(30).minusMonths(9).minusDays(30)));
		users.add(new User(++usersCount, "María", LocalDate.now().minusYears(36).minusMonths(5).minusDays(23)));
		users.add(new User(++usersCount, "Luis", LocalDate.now().minusYears(8).minusMonths(7).minusDays(6)));
		users.add(new User(++usersCount, "Lisa", LocalDate.now().minusYears(3)));
	}
	
	public List<User> getAllUsers() {
		return users;
	}
	
	public User getUserById(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().get();
	}
	
	public User saveUser(User user) {
		user.setId(++usersCount);
		users.add(user);
		return user;
	}
}

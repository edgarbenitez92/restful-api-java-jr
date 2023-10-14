package com.in28minutes.rest.webservices.restfulwebservices.helloword;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@GetMapping(path = "/hello-word")
	public String helloWordApi() {
		return "hello word";
	}

	@GetMapping(path = "/hello-word-bean")
	public HelloWordBean helloWordBeanApi() {
		return new HelloWordBean("hello word bean");
	}
}

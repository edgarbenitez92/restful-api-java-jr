package com.in28minutes.rest.webservices.restfulwebservices.helloword;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class HelloWorldController {

	private MessageSource messageSource;

	public HelloWorldController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@GetMapping(path = "/hello-word")
	public String helloWordApi() {
		return "hello word";
	}

	@GetMapping(path = "/hello-word-bean")
	public HelloWordBean helloWordBeanApi() {
		return new HelloWordBean("hello word bean");
	}

	@GetMapping(path = "/hello-word/path-variable/{name}")
	public HelloWordBean helloWordPathVariableApi(@PathVariable String name) {
		return new HelloWordBean(String.format("Hello word, %s", name));
		// return new HelloWordBean("hello word" + name);
	}

	@GetMapping(path = "/hello-word-i18n")
	public String helloWordApi18n() {
		Locale userLocale = LocaleContextHolder.getLocale();
		return messageSource.getMessage("good.morning.message", null, "Default Message", userLocale);
	}
}

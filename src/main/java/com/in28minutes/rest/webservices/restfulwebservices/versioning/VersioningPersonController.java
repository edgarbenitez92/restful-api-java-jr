package com.in28minutes.rest.webservices.restfulwebservices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersioningPersonController {

	@GetMapping("/v1/person")
	public PersonV1 getFirstVersionOfPerson() {
		return new PersonV1("Lisa Ramona");
	}

	@GetMapping("/v2/person")
	public PersonV2 getSecundVersionOfPerson() {
		return new PersonV2(new Name("Lisa", "Ramona"));
	}

	@GetMapping(path = "/person", params = "version=1")
	public PersonV1 getFirstVersionOfPersonRequestParams() {
		return new PersonV1("Lisa Ramona");
	}

	@GetMapping(path = "/person", params = "version=2")
	public PersonV2 getSecundVersionOfPersonRequestParams() {
		return new PersonV2(new Name("Lisa", "Ramona"));
	}
	
	@GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
	public PersonV1 getFirstVersionOfPersonRequestHeader() {
		return new PersonV1("Lisa Ramona");
	}
	
	@GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
	public PersonV2 getSecundVersionOfPersonRequestHeader() {
		return new PersonV2(new Name("Lisa", "Ramona"));
	}
	
	@GetMapping(path = "/person/accept", produces = "application/vnd.company.app-v1+json")
	public PersonV1 getFirstVersionOfPersonRequestAcceptHeader() {
		return new PersonV1("Lisa Ramona");
	}
	
	@GetMapping(path = "/person/accept", produces = "application/vnd.company.app-v2+json")
	public PersonV2 getSecundVersionOfPersonRequestAcceptHeader() {
		return new PersonV2(new Name("Lisa", "Ramona"));
	}
}

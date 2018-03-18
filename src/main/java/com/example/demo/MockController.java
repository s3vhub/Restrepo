package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.JsonObjects.AuthorizationToken;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@PropertySource(value = { "authenticationStatus.properties" })
public class MockController {

	@Autowired
	private Environment environment;

	@RequestMapping(value = "/token", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<Object> acceptAuthorization(
			@RequestHeader("Authorization") String auth) {
		if (environment.getProperty("authentication").equalsIgnoreCase("valid")) {
			ObjectMapper objectMapper = new ObjectMapper();
			InputStream autheResponseJson = getClass().getClassLoader()
					.getResourceAsStream("authResponse.json");
			AuthorizationToken authToken;
			try {
				authToken = objectMapper.readValue(autheResponseJson,
						AuthorizationToken.class);
				return new ResponseEntity<Object>(authToken, HttpStatus.OK);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);

	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}

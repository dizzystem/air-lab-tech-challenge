package com.dizzystem.airlab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpEntity;

@SpringBootApplication
@RestController
public class AirLabApplication {
	private static Airport[] airports;

	public static void main(String[] args) {
		SpringApplication.run(AirLabApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		RestTemplate restTemplate = new RestTemplate();
		URI uri;
		try {
			uri = new URI("https://open-atms.airlab.aero/api/v1/airac/airports");
		} catch (URISyntaxException x) {
			throw new IllegalArgumentException(x.getMessage(), x);
		}
		HttpHeaders headers = new HttpHeaders() {{
			set("api-key", "G9Tw58HE6HDzyq94HFmnd2yOymAuU32k2mEgL3oTVbhLl6E1opu5Hqxb5BASwCWv");
		}};
		HttpEntity<Airport[]> requestEntity = new HttpEntity<>(null, headers);
		
		ResponseEntity<Airport[]> response = restTemplate.exchange(
			uri, HttpMethod.GET, requestEntity, Airport[].class);
		airports = response.getBody();

		return airports[0].toString();
	}
}

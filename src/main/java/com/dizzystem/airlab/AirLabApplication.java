package com.dizzystem.airlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AirLabApplication {

	private static final Logger log = LoggerFactory.getLogger(AirLabApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AirLabApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		Airport[] airports = restTemplate.getForObject(
			"https://open-atms.airlab.aero/api/v1/airac/airports", Airport.class);
		
		return airports[0].toString();
	}
}

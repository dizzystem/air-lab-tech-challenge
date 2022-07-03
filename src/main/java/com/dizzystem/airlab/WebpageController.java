package com.dizzystem.airlab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@Controller
public class WebpageController {
	@GetMapping("/index")
	public String index(Model model) {
        return "index";
	}
}
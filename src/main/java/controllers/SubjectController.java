package controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {

	@GetMapping("/")
	public String test() {
		return "it works finally";
	}
}

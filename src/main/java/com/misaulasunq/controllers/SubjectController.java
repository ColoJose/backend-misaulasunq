package com.misaulasunq.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.misaulasunq.services.SubjectService;

@RestController
public class SubjectController {

	@Autowired
	private SubjectService subjectService;

	@GetMapping("/")
	public String test() {
		return "it works finally";
	}


}

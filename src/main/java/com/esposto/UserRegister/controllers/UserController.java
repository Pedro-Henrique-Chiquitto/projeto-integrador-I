package com.esposto.UserRegister.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.esposto.UserRegister.model.AppUser;
import com.esposto.UserRegister.model.RegisterDto;
import com.esposto.UserRegister.repositories.AppUserRepository;

import jakarta.validation.Valid;
@Controller
public class UserController {

	@Autowired
	private AppUserRepository repo;

	@GetMapping("/register")
	public String register(Model model) {
		RegisterDto registerDto = new RegisterDto();
		model.addAttribute(registerDto);
		model.addAttribute("success", false);
		return "register";
	}

	@PostMapping("/register")
	public String register(
			Model model,
			@Valid @ModelAttribute RegisterDto registerDto,
			BindingResult result
	) {

		if (result.hasErrors()) {
			return "register";
		}

		try {
			//criar uma nova conta
			var bCryptEncoder = new BCryptPasswordEncoder();

			AppUser newUser = new AppUser();
			newUser.setFirstName(registerDto.getFirstName());
			newUser.setLastName(registerDto.getLastName());
			newUser.setEmail(registerDto.getEmail());
			newUser.setPhone(registerDto.getPhone());
			newUser.setAddress(registerDto.getAddress());
			newUser.setRole("client");
			// Remova a linha newUser.setCreatedAt(new Date()); - não é necessária com @CreationTimestamp
			newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));

			repo.save(newUser);

			model.addAttribute("registerDto", new RegisterDto());
			model.addAttribute("success", true);
			return "redirect:/login?registered=true";

		} catch (Exception ex) {
			result.addError(new FieldError("registerDto", "firstName", ex.getMessage()));
			return "register";
		}


	}
}
package ru.jm.jmspringboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.jm.jmspringboot.model.User;
import ru.jm.jmspringboot.service.UserService;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/", produces = "application/json;charset=UTF-8")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/out")
	public String logout() {
		return "redirect:/logout";
	}

	@RequestMapping("/user")
	public String user(ModelMap model){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user=userService.getUser(auth.getName());
		user.emptyPassword();
		model.addAttribute("user",  user );
		return "userprofile";
	}

	@PatchMapping(value = "/user/{id}")
	public String updateUserProfile(@ModelAttribute("user") User user, @PathVariable("id") int id) {
		System.out.println("change user");
		userService.updateUserShort(id, user);
		return "redirect:/user";
	}

	@RequestMapping("/admin")
	public String admin(ModelMap model){
		model.addAttribute("users", userService.getAllUsers() );
		return "admin";
	}

	@GetMapping("/admin/{id}")
	public String adminEditUser(@PathVariable("id") int id, ModelMap model){
		User user= userService.getUser(id);
		user.emptyPassword();
		model.addAttribute("user",  user );
		return "updateuser";
	}

	@PatchMapping(value = "/admin/{id}")
	public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {

		userService.updateUser(id, user);
		return "redirect:/admin";
	}

	@GetMapping("/admin/createuser")
	public String adminCreateUser(ModelMap model){
		User user= new User();
		model.addAttribute("user",  user );
		return "createuser";
	}

	@PostMapping("/admin/create")
	public String adminCreateUSer(@ModelAttribute("user") User user){
		userService.saveUser(user);
		return "redirect:/admin";
	}

	@DeleteMapping(value = "/admin/{id}")
	public String deletUser(@PathVariable("id") int id) {

		userService.removeUserById(id);
		return "redirect:/admin";
	}

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		List<String> messages = new ArrayList<>();
		messages.add("Hello!");
		messages.add("I'm Spring MVC-SECURITY application");
		messages.add("5.2.0 version by sep'19 ");
		model.addAttribute("messages", messages);
		return "hello";
	}

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
		System.out.println("GET");
		return "login";
    }

	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String indexPage(ModelMap model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if ( auth.getName().indexOf("anonymousUser") == -1) {
			System.out.println("!"	+ auth.getName() );
			User user = userService.getUser(auth.getName());
			user.emptyPassword();
			model.addAttribute("user", user);


			StringBuilder message= new StringBuilder("Добро пожаловать:");
			message.append(user.getName()).append(" ").append(user.getLastName());

			model.addAttribute("message", message.toString());

		}

		return "index";
	}

}
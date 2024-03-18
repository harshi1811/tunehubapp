package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.Songs;
import com.example.demo.entities.Users;
import com.example.demo.services.SongsService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
	@Autowired
	UsersService userv;
	@Autowired
	SongsService sserv;
	
	@PostMapping("/register")
	public String addUser(@ModelAttribute Users user) {	
		boolean userstatus=userv.emailExists(user.getEmail());
		if(userstatus==false)
		{
			userv.addUser(user);
			return "registersuccess";
		}
		else
		{
			return "registerfail";
		}
	
	 
	}
	 
	 @PostMapping("/login")
	 public String validateUser(@RequestParam String email, @RequestParam String password,
			 HttpSession session)
	 {
		 session.setAttribute("email", email);
		 
		 if(userv.validateUser(email, password) == true)
		 {
			if(userv.getRole(email).equals("admin")) 
			{
				return "adminhome";
			}
			else 
			{
				return "customerhome";
			}
		 }
		 else
		 {
			 return "loginfail";
		 }
	 }
	 
	 
	 @GetMapping("/exploresongs")
	 public String exploreSongs(HttpSession session,Model model) {
		 String email=(String) session.getAttribute("email");
		Users user= userv.getUser(email);
		boolean userStatus=user.isPremium();
		if(userStatus==true) {
	List<Songs>	songslist=sserv.fetchAllSongs();
			model.addAttribute("songslist",songslist);
			return "displaysongs";
		}
		else {
			return "makepayment";
		}
	 }
	 
	 
		
	}


 


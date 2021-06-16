package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@GetMapping({"","/"})
	public String index() {
		
		//머스태치 기본폴더 -> src/main/resources/
		//뷰리졸버 설정:templates(prefix).mustache(suffix) -> 생략가능 의존 주입시
		return "index";
	}
	
	@GetMapping("/user")
	public String user() {
		return "user";
	}
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완료됨";
	}





}

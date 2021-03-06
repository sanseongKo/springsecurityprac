package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails) {//DI(의존성 주입)
		System.out.println("/test/login ===========");
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println("authentication: "+  principalDetails.getUser());
		
		System.out.println("userDetails: "+ userDetails.getUser());
		return "세션 정보 확인하기";
	}
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {//DI(의존성 주입)
		System.out.println("/test/login ===========");
		OAuth2User oauth2User = (OAuth2User)authentication.getPrincipal();
		System.out.println("authentication: "+  oauth2User.getAttributes());
		System.out.println("oauth2USer: "+ oauth.getAttributes());
		return "Oauth 세션 정보 확인하기";
	}
	
	@GetMapping({"","/"})
	public String index() {
		
		//머스태치 기본폴더 -> src/main/resources/
		//뷰리졸버 설정:templates(prefix).mustache(suffix) -> 생략가능 의존 주입시
		return "index";
	}
	
	//Oauth로그인을 해도 principalDetails로 받을 수 있고
	//일반 로그인을 해도 principalDetails로 받을 수 있다.
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: "+principalDetails.getUser());
		return "user";
	}
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	//스프링 시큐리티가 해당 주소를 낚는다
	@GetMapping("/loginForm")
	public String login() {
		return "loginForm";
	}
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user);// 비밀번호:1234 => 시큐리티로 로그인 x 이유는 패스워드가 암호화가 안되어 있기 때문
		
		return "redirect:/loginForm";//리다이렉트를 메소드를 호출
	}
	
	@Secured("ROLE_ADMIN")//@EnableGlobalMethodSecurity(securedEnabled=true) 이걸로 확인
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")//두개이상의 시큐리티를 사용할 때 넣어줄때
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
	
}

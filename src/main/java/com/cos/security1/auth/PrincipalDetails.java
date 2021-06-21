package com.cos.security1.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로긍니을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어 준다.(Security ContextHolder)
//오브젝트 타입=> Authentication 객체여야 들어갈 수 있다.
//Authentication 안에 User정보가 있어야 됨.
//User오브젝트타입 => UserDetails 타입 객체여야 한다.

//Security Session => Authentication => UserDetails(PrincipalDetails) 

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user;//콤포지션
	private Map<String, Object> attributes;
	//일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth 로그인
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	//해당 user의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}
	@Override
	public String getPassword() {
		
		return user.getPassword();
	}
	@Override
	public String getUsername() {
		
		return user.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}@Override
	public boolean isEnabled() {
		
		//우리 사이트!! 1년동안 회원 로그인 x 휴면계정으로
		
		return true;
	}
	@Override
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}

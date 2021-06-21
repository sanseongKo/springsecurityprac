package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest1: "+userRequest.getClientRegistration());//registratioId로 어떤 OAuth로 로그인이 되는지
		System.out.println("userRequest2: "+userRequest.getAccessToken());
		//우리가 구글 로그인버튼 클릭을 하면 -> 구글 로그인 창이 뜨고 -> 로그인 완료 -> 코드 리턴(OAuth-Client라이브러리) -> AccessToken요청
		//userRequest정보 -> loadUser 함수 호출 ->구글로부터 회원프로필 받아준다. 
		System.out.println("userRequest3: "+super.loadUser(userRequest).getAttributes());
		OAuth2User oauth2User = super.loadUser(userRequest); 
		return super.loadUser(userRequest);
	}
}

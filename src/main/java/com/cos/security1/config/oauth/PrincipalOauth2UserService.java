package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.config.oauth.provider.FacebookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest1: "+userRequest.getClientRegistration());//registratioId로 어떤 OAuth로 로그인이 되는지
		System.out.println("userRequest2: "+userRequest.getAccessToken());
		
		OAuth2User oauth2User = super.loadUser(userRequest); 
		//우리가 구글 로그인버튼 클릭을 하면 -> 구글 로그인 창이 뜨고 -> 로그인 완료 -> 코드 리턴(OAuth-Client라이브러리) -> AccessToken요청
		//userRequest정보 -> loadUser 함수 호출 ->구글로부터 회원프로필 받아준다. 
		System.out.println("userRequest3: "+super.loadUser(userRequest).getAttributes());
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else {
			System.out.println("우리는 구글과 페이스북만 지원해요");
		}
		
		String provider = oAuth2UserInfo.getProvider();//userRequest.getClientRegistration().getRegistrationId();
		String providerId = oAuth2UserInfo.getProviderId();//oauth2User.getAttribute("sub");
		String email = oAuth2UserInfo.getEmail();
		String username = provider +"_" + providerId;
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(providerId)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());//authentication 객체에 들어감
	}
}

package com.kakao.saramaracommunity.auth;

import com.kakao.saramaracommunity.auth.dto.OAuthAttributes;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 로그인에 성공한 사용자 데이터를 데이터베이스에 삽입하는 서비스
 * 구글, 네이버 로그인 이후 가져온 사용자의 정보(email, name, picture) 등을 기반으로 가입 및 정보수정 등을 수행한다.
 *
 * registrationId: 현재 로그인 진행 중인 서비스를 구분하는 코드이다. 구글 로그인인지, 네이버 로그인인지 구분하기 위해 사용한다.
 * userNameAttributeName: OAuth2 로그인 진행 시 키가 되는 필드값을 뜻한다. 기본 키와 같은 의미이다.
 *      구글의 경우 기본적으로 코드를 지원하지만 네이버, 카카오 등은 기본으로 제공되지 않는다. 구글에서 제공하는 userNameAttributeName의 기본값은 sub이다.
 * OAuthAttributes(dto): OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 DTO 클래스이다.
 * Member Entity의 update 메소드 추가: 사용자 정보가 업데이트 되었을 경우르 대비하여 update 메소드를 Member Entity에 추가하였다.
 */

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegrate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegrate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);

//        JWT 도입으로 인한 세션 미사용
//        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getEmail(), attributes.getNickname(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        System.out.println(member);
        return memberRepository.save(member);
    }
}

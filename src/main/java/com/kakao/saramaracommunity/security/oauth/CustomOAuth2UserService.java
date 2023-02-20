package com.kakao.saramaracommunity.security.oauth;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kakao.saramaracommunity.exception.OAuthProcessingException;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.persistence.MemberRepository;
import com.kakao.saramaracommunity.security.oauth.userinfo.OAuth2UserInfo;
import com.kakao.saramaracommunity.security.oauth.userinfo.OAuth2UserInfoFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    // OAuth2UserRequest에 있는 Access Token으로 유저정보 get
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return process(oAuth2UserRequest, oAuth2User);
    }

    // 획득한 유저정보를 Java Model과 맵핑하고 프로세스 진행
    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {


        Type authType = Type.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authType, oAuth2User.getAttributes());

        if (userInfo.getEmail().isEmpty()) {
            throw new OAuthProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Member> existMember = memberRepository.getWithRolesEqualOAuth(userInfo.getEmail(), authType);
        Member authMember;
        if (existMember.isPresent()) { //가입된 경우
            authMember = existMember.get();
        }
        else {
            authMember = createUser(userInfo, authType);
        }

        // 가입되어 있으면 찾아오기
        //Member authMember = existMember.get();
        //createUser(userInfo, authType);
        return CustomOAuthUserDetails.create(authMember, oAuth2User.getAttributes());
    }

    // 회원가입 처리
    private Member createUser(OAuth2UserInfo userInfo, Type authType) {
        Member member = Member.builder()
                .email(userInfo.getEmail())
                .picture(userInfo.getImageUrl())
                .role(Collections.singleton(Role.USER))
                .type(authType)
                .build();
        return memberRepository.save(member);
    }

}

package com.kakao.saramaracommunity.auth.dto;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;


/**
 *
 * of() static method: OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 반환해야 한다.
 * toEntity: Member Entity를 생성하는데 OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때이다.
 *      가입할 때 기본 권한을 GUEST로 부여하기 위해서 role 빌더값에는 Role.GUEST로 지정한다.
 * ofGoogle: 구글 소셜 로그인
 * ofNaver: 네이버 소셜 로그인
 */

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private Type type;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, Type type, String nameAttributeKey, String nickname, String email, String picture) {
        this.attributes = attributes;
        this.type = type;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        System.out.println("registrationId: " + registrationId);
        System.out.println("userNameAttributeName: " + userNameAttributeName);
        System.out.println("attributes: " + attributes);

        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .type(Type.GOOGLE)
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .type(Type.NAVER)
                .nickname((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .type(type)
                .nickname(nickname)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}

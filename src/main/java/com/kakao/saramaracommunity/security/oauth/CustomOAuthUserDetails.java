package com.kakao.saramaracommunity.security.oauth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kakao.saramaracommunity.member.entity.Member;

import lombok.Getter;

@Getter
public class CustomOAuthUserDetails implements UserDetails, OAuth2User {
    private Long memberId;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomOAuthUserDetails(Long memberId, String email, Collection<? extends GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.email = email;
        this.authorities = authorities;
    }

    public static CustomOAuthUserDetails create(Member member) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomOAuthUserDetails(
                member.getMemberId(),
                member.getEmail(),
                authorities
        );
    }

    public static CustomOAuthUserDetails create(Member member, Map<String, Object> attributes) {
        CustomOAuthUserDetails userDetails = CustomOAuthUserDetails.create(member);
        userDetails.setAttributes(attributes);
        return userDetails;
    }


    // UserDetail Override
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User Override
    @Override
    public String getName() {
        return String.valueOf(memberId);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

//package com.kakao.saramaracommunity.member.dto;
//
//import com.kakao.saramaracommunity.member.entity.Member;
//import com.kakao.saramaracommunity.member.entity.Role;
//import com.kakao.saramaracommunity.member.entity.Type;
//import lombok.*;
//
//
//@NoArgsConstructor
//@ToString
//@Getter
//public class MemberSaveRequestDto {
//    private Type type;
//    private String email;
//    private String nickname;
//    private String password;
//    private Role role;
//    private String picture;
//
//    @Builder
//    public MemberSaveRequestDto(Type type, String email, String nickname, String password, Role role, String picture) {
//        this.type = type;
//        this.email = email;
//        this.nickname = nickname;
//        this.password = password;
//        this.role = role;
//        this.picture = picture;
//    }
//
//    public Member toEntity() {
//        return Member.builder()
//                .type(type)
//                .email(email)
//                .nickname(nickname)
//                .password(password)
//                .role(role)
//                .picture(picture)
//                .build();
//    }
//}

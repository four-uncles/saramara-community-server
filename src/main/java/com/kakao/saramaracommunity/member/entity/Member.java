package com.kakao.saramaracommunity.member.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
public class Member extends BaseTimeEntity {

   @Id
   @Column(nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, length = 100, unique = true)
   private String email;

   @Column(length = 100)
   private String password;

   @Column(length = 10, unique = true)
   private String nickname;

   @Builder
   public Member(String email, String password, String nickname) {
      this.email = email;
      this.password = password;
      this.nickname = nickname;
   }

   public static Member of (MemberRegisterRequest newMemberInfo) {
      return Member.builder()
          .email(newMemberInfo.email())
          .password(newMemberInfo.password())
          .nickname(newMemberInfo.nickname())
          .build();
   }
}

package com.kakao.saramaracommunity.member.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

   @Id
   @Column(name = "member_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, length = 100, unique = true)
   private String email;

   @Column(length = 100)
   private String password;

   @Column(length = 10, unique = true)
   private String nickname;

   @Builder
   public Member(
           final String email,
           final String password,
           final String nickname
   ) {
      this.email = email;
      this.password = password;
      this.nickname = nickname;
   }

}

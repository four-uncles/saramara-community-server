package com.kakao.saramaracommunity.member.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Persistable;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import com.kakao.saramaracommunity.member.controller.request.MemberRegisterRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update member set deleted_at = CURRENT_TIMESTAMP where member_id = ?")
@DynamicUpdate
@Entity
public class Member extends BaseTimeEntity {

   @Id
   @Column(nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long memberId;

   @Column(nullable = false, length = 100, unique = true)
   private String email;

   @Column(length = 100)
   private String password;

   @Column(length = 10, unique = true)
   private String nickname;

   @Builder
   private Member(String email, String password, String nickname) {
      this.email = email;
      this.password = password;
      this.nickname = nickname;
   }

   public static Member register (MemberRegisterRequest newMemberInfo) {
      return Member.builder()
          .email(newMemberInfo.email())
          .password(newMemberInfo.password())
          .nickname(newMemberInfo.nickname())
          .build();
   }
}

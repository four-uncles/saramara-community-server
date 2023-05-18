package com.kakao.saramaracommunity.member.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update member set deleted_at = CURRENT_TIMESTAMP where member_id = ?")
@Entity
public class Member extends BaseTimeEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long memberId;

   @Column(nullable = false, length = 50, unique = true)
   private String email;

   @Column(length = 100)
   private String password;
   @Column(length = 50, unique = true)
   private String nickname;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Type type;

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private Role role;
   @ElementCollection(fetch = FetchType.LAZY)
   @Builder.Default
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Set<Role> role = new HashSet<>();

   private String picture;

//    private String token;
   private String refreshToken;

   @Builder
   public Member(Type type, String nickname, String password, Set<Role> role, String picture) {
      this.type = type;
      //this.email = email;
      this.nickname = nickname;
      this.password = password;
      this.role = role;
      this.picture = picture;
   }

//     @Builder
//     public Member(Type type, Long memberId, String email, String nickname, String password, Role role, String picture) {
//         this.type = type;
//         this.memberId = memberId;
//         this.email = email;
//         this.nickname = nickname;
//         this.password = password;
//         this.role = role;
//         this.picture = picture;
//     }

    public Member update(String email, String nickname, String picture) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
  
    public void changePassword(String password) {
        this.password = password;
    }
  
    public void changeEmail(String email) {
        this.email = email;
    }
}

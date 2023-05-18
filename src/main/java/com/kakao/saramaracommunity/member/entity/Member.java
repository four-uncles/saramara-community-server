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

    /**
     * 이하 주석 코드는 OAuth2 연동시 개발한 코드입니다.
     * 현범님 Spring Security + JWT 코드 병합이 우선이라 생각되어 제가 작성한 Member의 메소드들을 주석으로 변경했습니다.
     * OAuth2 코드를 현범님 작성 코드 위주로 고쳐가야 할 것 같아 제 코드는 임시로 주석 처리하는 것이 맞다고 판단했습니다.
     *
     */
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

//    public Member update(String email, String nickname, String picture) {
//        this.email = email;
//        this.nickname = nickname;
//        this.picture = picture;
//        return this;
//    }

//    public String getRoleKey() {
//        return this.role.getKey();
//    }
  
    public void changePassword(String password) {
        this.password = password;
    }
  
    public void changeEmail(String email) {
        this.email = email;
    }
}

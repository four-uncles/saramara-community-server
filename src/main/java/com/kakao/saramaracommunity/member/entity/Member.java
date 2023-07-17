package com.kakao.saramaracommunity.member.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"type", "role", "memberImage"})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update member set deleted_at = CURRENT_TIMESTAMP where member_id = ?")
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

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Type type;
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<Role> role;

    //cascade = CascadeType.ALL
    // 회원가입 시 Member 를 저장하기 연관 테이블에 해당되는 Entity를 필드로 가지고 있으며, JPA를 이용하기 때문에
    // MemberImage를 먼저 저장하고 Member를 저장하지 않으면, hibernate.TransientPropertyValueException 가 발생한다.
    // 해결 방법은 전자가 있고 cascade = CascadeType.ALL 를 선언해 연관 엔티티를 바로 저장하도록 하는 방법이 있다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MemberImage memberImage;

   //private String token;
   //private String refreshToken;

   @Builder
   public Member(String email,Type type, String nickname, String password, Set<Role> role, MemberImage memberImage) {
      this.type = type;
      this.email = email;
      this.nickname = nickname;
      this.password = password;
      this.role = role;
      this.memberImage = memberImage;
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
  
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}

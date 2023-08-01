package com.kakao.saramaracommunity.member.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;

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

   /*
    Member 테이블에 관련된 Entity 클래스에 해당되는 테이블들은
    Member 에 관련한 정보를 Member Entity 에서 접근을 시작할 수 있게 하면 Join 연산을 감소시킬 수 있다. (일관성, 효율성)

    MemberImage 조회 시 Member의 정보가 일반적으로 필요하나, Member 만을 접근할 때 MemberImage는 필요 없는 경우가 있어서
    Member 는 MemberImage를 알고 있되, MemberImage는 Member를 모르게 하는 단방향 참조를 함으로써 상호 의존성을 낮춘다.
    또한 지연로딩을 사용하면 MemberImage가 필요없는 Member 조회 시, 조회를 하지 않으므로 상호의존성을 낮출 수 있다.

    상호 의존성이 높으면 두 클래스 중 하나를 조회할 때 실제 값이 필요하지 않으나, 그 클래스의 정보를 알아야 한다는 것을 의미한다.

    또한 아래의 cascade = CascadeType.ALL 을 사용하면 불필요한 Member 와 관련된 테이블의 Repository 계층을 생성할 필요가 적어지는 것 같다.
   */


    //@ElementCollection(fetch = FetchType.LAZY) 은 DB에 Enrity가 아닌 값, 타입에 해당되는 Collection, List, Set, Map 등을 저장하기 위해 사용되며, 새로운 테이블로 매핑된다.
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
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

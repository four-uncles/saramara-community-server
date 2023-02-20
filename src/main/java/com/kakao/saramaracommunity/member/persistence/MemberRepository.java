package com.kakao.saramaracommunity.member.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Type;

public interface MemberRepository extends JpaRepository<Member, Long> {

   // username 을 기준으로 UserEntity 정보를 가져올 때, AuthorityEntity 와 UserEntity 에 대응 되는 DB 객체를 조인해서 생성한
   // user_authorities 의 정보를 가져온다.
   // @EntityGraph 는 쿼리 수행시 지연로딩 방식이 아니라 Eager 방식으로 authorities 정보를 가져오게 된다.

   // Local 에서 회원가입 시 Member 테이블의 type 이 LOCAL인 회원 중 해당되는 email로 가입되어 있는지 확인할 때 사용
   // Local 에서 로그인 시 Member 테이블에 해당되는 email 과 Local 에서 가입한 회원이 맞는지 여부를 가릴 때도 사용
   @EntityGraph(attributePaths = "role") // 아래의 쿼리 메서드 실행시 role 테이블 을 지연로딩 하지 않고 같이 가져오도록 지정
   @Query("select m from Member m where m.email = :email and m.type = 'LOCAL' ")
   Optional<Member> getWithRolesEqualLocal(String email);

   // OAuth 를 이용해서 로그인 할 때 최초 로그인이면 자동으로 회원가입을 진행하는데 이때 이미 해당 OAuth 로그인의 email 로 가입되어 있는지 확일 할 때 사용
   @EntityGraph(attributePaths = "role")
   @Query("select m from Member m where m.email = :email and m.type = :type ")
   Optional<Member> getWithRolesEqualOAuth(String email, Type type);


   // 사용자가 로그인 시 발급 받은 토큰을 가지고 Controller 단에서 인가를 함과 동시에 유저의 권한을 포함한 email, nickname, picture 등의 정보를 알려줄 때 사용
   @EntityGraph(attributePaths = "role")
   @Query("select m from Member m where m.email = :email and (m.type = 'LOCAL' or m.type = 'KAKAO' or m.type = 'NAVER' or m.type = 'GOOGLE') ")
   Optional<Member> getWithRoles(String email);

   Optional<Member>findByEmail(String email);

}

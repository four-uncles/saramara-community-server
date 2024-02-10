package com.kakao.saramaracommunity.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakao.saramaracommunity.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findMemberByEmail(String email);

	boolean existsMemberByEmail(String email);
}

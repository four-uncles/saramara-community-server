package com.kakao.saramaracommunity.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakao.saramaracommunity.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findMemberByEmail(String email);

	boolean existsMemberByEmail(String email);
}

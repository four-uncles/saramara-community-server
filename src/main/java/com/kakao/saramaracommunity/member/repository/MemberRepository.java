package com.kakao.saramaracommunity.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakao.saramaracommunity.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}

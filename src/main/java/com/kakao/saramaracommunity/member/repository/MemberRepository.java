package com.kakao.saramaracommunity.member.repository;

import com.kakao.saramaracommunity.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}

package com.kakao.saramaracommunity.domain.member;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 사용자_등록() {
        // given
        memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("lango@kakao.com")
                .nickname("lango")
                .password("kakao")
                .role(Role.BASIC)
                .profileImage("imagePath")
                .build());

        // when
        List<Member> memberList = memberRepository.findAll();

        // then
        Member member = memberList.get(0);
        assertThat(member.getEmail()).isEqualTo("lango@kakao.com");
    }
}

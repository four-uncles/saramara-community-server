package com.kakao.saramaracommunity.member.service;

import com.kakao.saramaracommunity.member.dto.MemberSaveRequestDto;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberSerivce memberSerivce;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 사용자_회원가입() throws Exception {
        // given
        MemberSaveRequestDto memberRegisterDTO = MemberSaveRequestDto.builder()
                .type(Type.LOCAL)
                .nickname("lango")
                .email("lango@kakao.com")
                .password("test123")
                .role(Role.USER)
                .picture("test")
                .build();
        Long newMemberId = memberSerivce.register(memberRegisterDTO);

        // when
        Optional<Member> member = memberRepository.findById(newMemberId);

        // then
        assertThat(member.get().getEmail()).isEqualTo("lango@kakao.com");
    }
}

package com.kakao.saramaracommunity.member.repository;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void clean() {
        memberRepository.deleteAll();
    }
    @Test
    public void save() {
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
        System.out.println(member);
        assertThat(member.getEmail()).isEqualTo("lango@kakao.com");
    }

    @Test
    public void deleteById() {
        // soft delete 방식으로 삭제되어야 한다.
        // given
        Member member1 = memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("del1@kakao.com")
                .nickname("del1")
                .password("kakao")
                .role(Role.BASIC)
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("del2@kakao.com")
                .nickname("del2")
                .password("kakao")
                .role(Role.BASIC)
                .build());

        Long member1Id = member1.getMemberId();
        Long member2Id = member2.getMemberId();
        // member1과 member2를 삭제할 때 soft delete가 적용되어 deltedAt 컬럼에 값이 설정된다.
        memberRepository.deleteById(member1Id);
        memberRepository.deleteById(member2Id);

        // when
        // 삭제 이후 member1과 member2는 조회되지 않는다.
        int allMemberCnt = (int) memberRepository.count();
        System.out.println("all member cnt: " + allMemberCnt);

        // then
        assertThat(allMemberCnt).isEqualTo(0);
    }

    @Test
    public void findAll() {
        //given
        Member member = memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("lango@kakao.com")
                .nickname("lango")
                .password("kakao")
                .role(Role.BASIC)
                .build());
        memberRepository.save(member);

        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Member result = memberList.get(0);
        assertThat(result.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void findById() {
        //given
        Member member = memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("lango@kakao.com")
                .nickname("lango")
                .password("kakao")
                .role(Role.BASIC)
                .build());
        memberRepository.save(member);

        //when
        Optional<Member> optional = memberRepository.findById(member.getMemberId());
        Member result = optional.get();

        //then
        assertThat(result.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void findByEmail() {
        //given
        Member member = memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("lango@kakao.com")
                .nickname("lango")
                .password("kakao")
                .role(Role.BASIC)
                .build());
        memberRepository.save(member);

        //when
        Optional<Member> optional = memberRepository.findByEmail(member.getEmail());
        Member result = optional.get();

        //then
        assertThat(result.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void update() {
        //given
        Member member = memberRepository.save(Member.builder()
                .type(Type.LOCAL)
                .email("lango@kakao.com")
                .nickname("lango")
                .password("kakao")
                .role(Role.BASIC)
                .build());
        memberRepository.save(member);

        Member updateMember = Member.builder()
                .type(member.getType())
                .email("lango@naver.com")
                .nickname(member.getNickname())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
        memberRepository.save(updateMember);
        //when
        List<Member> memberList = memberRepository.findAll();
        Member result = memberList.get(0);

        //then
        assertThat(result.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
    }
}

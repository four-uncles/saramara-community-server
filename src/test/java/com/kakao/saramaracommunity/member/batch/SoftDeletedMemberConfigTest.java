/*
package com.kakao.saramaracommunity.member.batch;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

@SpringBootTest
public class SoftDeletedMemberConfigTest {
	// JobLauncherTestUtils 는 Spring Container 에 Bean으로 등록되어 있지 않아서 직접 수행
	@Configuration
	static class TestConfig{
		@Bean
		public JobLauncherTestUtils jobLauncherTestUtils(){
			return new JobLauncherTestUtils();
		}
	}
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@MockBean
	private MemberRepository memberRepository;

	@AfterEach
	void tearDown(){
		memberRepository.deleteAllInBatch();
	}
	@Test
	public void testSoftDeleteToDeleteJob() throws Exception {
		// Given
		LocalDateTime threshold = LocalDateTime.now().minusDays(4);

		// 가짜(Mock) Member 객체 생성
		Member member1 = new Member();
		member1.setEmail("test99@test.com");
		member1.setDeletedAt(threshold);

		Member member2 = new Member();
		member2.setEmail("test100@test.com");
		member2.setDeletedAt(threshold);

		// MemberRepository의 findByEmail 메서드에 대한 Mock 동작 정의
		Mockito.when(memberRepository.findByEmail("test99@test.com")).thenReturn(member1);
		Mockito.when(memberRepository.findByEmail("test100@test.com")).thenReturn(member2);

		// When
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// Then
		BatchStatus batchStatus = jobExecution.getStatus();
		assert (batchStatus == BatchStatus.COMPLETED);
	}
}
*/

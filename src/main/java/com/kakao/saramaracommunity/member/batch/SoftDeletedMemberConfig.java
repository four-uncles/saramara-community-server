package com.kakao.saramaracommunity.member.batch;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SoftDeletedMemberConfig {


	private final MemberRepository memberRepository;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final JobLauncher jobLauncher;

	@Bean
	public Job softDeletedToDeleteJob(JobRepository jobRepository,Step step){
		return new JobBuilder("softDeletedToDeleteJob", jobRepository)
			.start(step)
			.build();
	}

	@Bean
	public Step softDeletedToDeletedStep(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager transactionManager){
		return new StepBuilder("softDeletedToDeleteStep", jobRepository)
			.tasklet(tasklet, transactionManager)
			.build();
	}

	@Bean
	public Tasklet softDeleteTasklet() {
		return (contribution, chunkContext) -> {
			LocalDateTime threshold = LocalDateTime.now().minusDays(3);
			List<Member> expiredMembers = memberRepository.findByDeletedAtBefore(threshold);

			// 연관된 테이블의 정보 삭제 작업 수행
			// ...

			// 회원 테이블에서 삭제
			memberRepository.deleteAll(expiredMembers);

			return RepeatStatus.FINISHED;
		};
	}


	// 매일 2시에 실행
	@Scheduled(cron = "0 2 * * *")
	public void runSoftDeleteJob() throws
		JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("JobID", String.valueOf(System.currentTimeMillis()))
			.toJobParameters();

		JobExecution jobExecution = jobLauncher.run(
			softDeletedToDeleteJob(jobRepository, softDeletedToDeletedStep(
					jobRepository, softDeleteTasklet(), transactionManager
				)
			), jobParameters);
		// 추가 작업 또는 로깅 등이 필요한 경우 처리 가능
	}
}

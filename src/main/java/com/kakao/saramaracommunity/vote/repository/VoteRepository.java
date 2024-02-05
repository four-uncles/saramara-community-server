package com.kakao.saramaracommunity.vote.repository;

import com.kakao.saramaracommunity.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}

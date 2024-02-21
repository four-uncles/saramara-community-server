package com.kakao.saramaracommunity.vote.service;

import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_IMAGE_NOT_FOUND;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.vote.dto.business.request.VoteCreateServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;
import com.kakao.saramaracommunity.vote.entity.Vote;
import com.kakao.saramaracommunity.vote.repository.VoteRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final BoardRepository boardRepository;

    private final BoardImageRepository boardImageRepository;

    private final MemberRepository memberRepository;

    @Override
    public VoteCreateResponse createVote(VoteCreateServiceRequest request) {
        log.info("[VoteServiceImpl.class] 요청에 따라 투표를 시도합니다.");
        Vote vote = voteRepository.save(
                request.toEntity(
                    getMemberEntity(request.memberId()),
                    getBoardEntity(request.boardId()),
                    getBoardImageEntity(request.boardImageId())
                )
        );
        log.info("[VoteServiceImpl.class] 요청에 따라 투표를 생성하였습니다.");
        return VoteCreateResponse.of(vote);
    }

    private Member getMemberEntity(Long memberId) {
        return Optional.ofNullable(memberId)
                .flatMap(memberRepository::findById)
                .orElseThrow(() -> new MemberBusinessException(MEMBER_NOT_FOUND));
    }

    private Board getBoardEntity(Long boardId) {
        return Optional.ofNullable(boardId)
                .flatMap(boardRepository::findById)
                .orElseThrow(() -> new BoardBusinessException(BOARD_NOT_FOUND));
    }

    private BoardImage getBoardImageEntity(Long boardImageId) {
        return Optional.ofNullable(boardImageId)
                .flatMap(boardImageRepository::findById)
                .orElseThrow(() -> new BoardBusinessException(BOARD_IMAGE_NOT_FOUND));
    }

}

package com.kakao.saramaracommunity.vote.service;

import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_IMAGE_NOT_FOUND;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;
import static com.kakao.saramaracommunity.vote.exception.VoteErrorCode.VOTE_NOT_FOUND;

import com.kakao.saramaracommunity.board.entity.Board;
import com.kakao.saramaracommunity.board.entity.BoardImage;
import com.kakao.saramaracommunity.board.exception.BoardBusinessException;
import com.kakao.saramaracommunity.board.repository.BoardImageRepository;
import com.kakao.saramaracommunity.board.repository.BoardRepository;
import com.kakao.saramaracommunity.member.entity.Member;
import com.kakao.saramaracommunity.member.exception.MemberBusinessException;
import com.kakao.saramaracommunity.member.repository.MemberRepository;
import com.kakao.saramaracommunity.vote.dto.business.request.VoteCreateServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.request.VoteDeleteServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.request.VoteUpdateServiceRequest;
import com.kakao.saramaracommunity.vote.dto.business.response.VoteCreateResponse;
import com.kakao.saramaracommunity.vote.dto.business.response.VotesReadInBoardResponse;
import com.kakao.saramaracommunity.vote.entity.Vote;
import com.kakao.saramaracommunity.vote.exception.VoteBusinessException;
import com.kakao.saramaracommunity.vote.repository.VoteRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    @Transactional(readOnly = true)
    public VotesReadInBoardResponse readVoteInBoard(Long memberId, Long boardId) {
        log.info("[VoteServiceImpl.class] 요청에 따라 게시글의 투표 조회를 시도합니다.");

        validateVoter(memberId, boardId);

        List<Object[]> votes = voteRepository.getVotesByBoard(boardId);
        Map<String, Long> voteCounts = getVoteCounts(votes);
        Long totalVotes = calculateTotalVotes(voteCounts);

        log.info("[VoteServiceImpl.class] 요청에 따라 게시글 투표 상황을 조회하였습니다.");
        return new VotesReadInBoardResponse(boardId, totalVotes, voteCounts);
    }

    @Override
    public void updateVote(Long voteId, VoteUpdateServiceRequest request) {
        log.info("[VoteServiceImpl.class] 요청에 따라 투표 수정을 시도합니다.");
        Vote savedVote = getVoteEntity(voteId);
        verifyVoter(savedVote, request.memberId());
        savedVote.changeVote(request.memberId(), request.boardImage());
        log.info("[VoteServiceImpl.class] 요청에 따라 투표를 수정 하였습니다.");
    }

    @Override
    public void deleteVote(Long voteId, VoteDeleteServiceRequest request) {
        log.info("[VoteServiceImpl.class] 요청에 따라 투표 삭제를 시도합니다.");
        Vote savedVote = getVoteEntity(voteId);
        verifyVoter(savedVote, request.memberId());
        voteRepository.delete(savedVote);
        log.info("[VoteServiceImpl.class] 요청에 따라 투표를 삭제 하였습니다.");
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

    private Vote getVoteEntity(Long voteId) {
        return Optional.ofNullable(voteId)
                .flatMap(voteRepository::findById)
                .orElseThrow(() -> new VoteBusinessException(VOTE_NOT_FOUND));
    }

    private BoardImage getBoardImageEntity(Long boardImageId) {
        return Optional.ofNullable(boardImageId)
                .flatMap(boardImageRepository::findById)
                .orElseThrow(() -> new BoardBusinessException(BOARD_IMAGE_NOT_FOUND));
    }

    private Map<String, Long> getVoteCounts(List<Object[]> votes) {
        Map<String, Long> voteCounts = new LinkedHashMap<>();

        for (Object[] vote : votes) {
            String imagePath = (String) vote[0]; // 이미지 경로
            Long count = (Long) vote[1]; // 투표 수
            voteCounts.put(imagePath, count);
        }
        return voteCounts;
    }

    private Long calculateTotalVotes(Map<String, Long> voteCounts) {
        Long totalVotes = 0L;
        for (Long count : voteCounts.values()) {
            totalVotes += count;
        }
        return totalVotes;
    }

    private void validateVoter(Long memberId, Long boardId) {
        Optional<Vote> existVote = voteRepository.findByMemberIdAndBoardId(memberId, boardId);
        if (existVote.isPresent()) {
            log.info("[VoteServiceImpl.class] 해당 게시글의 투표자 검증이 완료되었습니다.");
        } else {
            log.info("[VoteServiceImpl.class] 투표 상태를 조회할 수 있는 상태가 아닙니다. 투표를 완료해주세요.");
            throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
        }
    }

    private void verifyVoter(Vote vote, Long memberId) {
        Member voter = vote.getMember();
        if (memberId == null) {
            throw new MemberBusinessException(MEMBER_NOT_FOUND);
        }
        if (!voter.getId().equals(memberId)) {
            throw new MemberBusinessException(UNAUTHORIZED_TO_MEMBER);
        }
    }

}

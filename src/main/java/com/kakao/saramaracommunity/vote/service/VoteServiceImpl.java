package com.kakao.saramaracommunity.vote.service;

import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_IMAGE_NOT_FOUND;
import static com.kakao.saramaracommunity.board.exception.BoardErrorCode.BOARD_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.kakao.saramaracommunity.member.exception.MemberErrorCode.UNAUTHORIZED_TO_MEMBER;
import static com.kakao.saramaracommunity.vote.exception.VoteErrorCode.VOTE_ALREADY_EXISTS;
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
import java.security.Principal;
import java.util.HashMap;
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
        log.info("[VoteServiceImpl] 요청에 따라 투표를 시도합니다.");
        verifyDuplicateVote(request.memberId(), request.boardId());
        Vote vote = voteRepository.save(
                request.toEntity(
                    getMemberEntity(request.memberId()),
                    getBoardEntity(request.boardId()),
                    getBoardImageEntity(request.boardImageId())
                )
        );
        log.info("[VoteServiceImpl] 요청에 따라 투표를 생성하였습니다.");
        return VoteCreateResponse.of(vote);
    }

    @Override
    @Transactional(readOnly = true)
    public VotesReadInBoardResponse readVoteInBoard(Long boardId, Principal principal) {
        Board savedBoard = getBoardEntity(boardId);
        log.info("[VoteServiceImpl] 요청에 따라 게시글의 투표 조회를 시도합니다.");
        boolean isVoted = false;

        //  Principal 객체가 null이 아니라면, 현재 로그인한 유저가 있는 것이므로, 해당 유저의 정보를 가져온다.
        if (principal != null) {
            Long memberId = getMemberInfo(principal);
            isVoted = isMemberVoted(memberId, savedBoard.getId());
        }
        List<Object[]> votes = voteRepository.getVotesByBoard(savedBoard.getId());
        Map<String, Long> voteCounts = getVoteCounts(votes);
        Long totalVotes = calculateTotalVotes(voteCounts);

        log.info("[VoteServiceImpl] 요청에 따라 게시글 투표 상황을 조회하였습니다.");
        return VotesReadInBoardResponse.of(savedBoard.getId(), isVoted, totalVotes, voteCounts);
    }

    @Override
    public void updateVote(Long voteId, VoteUpdateServiceRequest request) {
        log.info("[VoteServiceImpl] 요청에 따라 투표 수정을 시도합니다.");
        Vote savedVote = getVoteEntity(voteId);
        verifyVoter(savedVote, request.memberId());
        savedVote.changeVote(request.memberId(), request.boardImage());
        log.info("[VoteServiceImpl] 요청에 따라 투표를 수정 하였습니다.");
    }

    @Override
    public void deleteVote(Long voteId, VoteDeleteServiceRequest request) {
        log.info("[VoteServiceImpl] 요청에 따라 투표 삭제를 시도합니다.");
        Vote savedVote = getVoteEntity(voteId);
        verifyVoter(savedVote, request.memberId());
        voteRepository.delete(savedVote);
        log.info("[VoteServiceImpl] 요청에 따라 투표를 삭제 하였습니다.");
    }

    /**
     * 중복 투표 방지를 위한 검증 메서드
     */
    private void verifyDuplicateVote(Long memberId, Long boardId) {
        if (isMemberVoted(memberId, boardId)) {
            throw new VoteBusinessException(VOTE_ALREADY_EXISTS);
        }
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

    /**
     * 로그인한 회원 고유 식별자를 가져오기 위한 메서드
     */
    private Long getMemberInfo(Principal principal) {
        return memberRepository.findMemberByEmail(principal.getName())
                .orElseThrow(() -> new MemberBusinessException(MEMBER_NOT_FOUND))
                .getId();
    }

    /**
     * 투표를 진행한 회원인지 확인하기 위한 메서드
     */
    public Boolean isMemberVoted(Long memberId, Long boardId) {
        return voteRepository.findByMemberIdAndBoardId(memberId, boardId).isPresent();
    }

    private Map<String, Long> getVoteCounts(List<Object[]> votes) {
        Map<String, Long> voteCounts = new HashMap<>();

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

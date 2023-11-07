package com.kakao.saramaracommunity.comment.controller.dto.request;

import com.kakao.saramaracommunity.comment.service.dto.request.CommentCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCreateRequset {

    // 댓글을 등록하기전인데 만들어지지 않은 댓글의 번호를 알 수 없다. 고로 commentId는 필요없는 필드이다.
    private Long commentId;

    @NotNull(message = "회원정보은 필수입니다.")
    private Long memberId;

    @NotNull(message = "게시글 번호는 필수입니다.")
    private Long boardId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotNull(message = "투표를 선택해주세요.")
    private Long pick;

    // 댓글 생성일자와 수정일자를 요청으로 받을 필요가 있는가?
    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @Builder
    private CommentCreateRequset(Long commentId, Long memberId, Long boardId, String content, Long pick, LocalDateTime regDate, LocalDateTime modDate) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.boardId = boardId;
        this.content = content;
        this.pick = pick;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    public CommentCreateServiceRequest toServiceRequest() {
        return CommentCreateServiceRequest.builder()
                .commentId(commentId)
                .memberId(memberId)
                .boardId(boardId)
                .content(content)
                .pick(pick)
                .regDate(regDate)
                .modDate(modDate)
                .build();
    }
}

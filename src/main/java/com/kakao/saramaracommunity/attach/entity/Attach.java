package com.kakao.saramaracommunity.attach.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * Attach: 이미지 첨부파일 관리 테이블
 * attachId: 해당 테이블의 PK 필드
 * type: AttachType(Board or Comment)을 속성으로 가지는 필드
 * id: Long형의 Board의 boardId나 Comment의 commentId를 속성으로 가지는 필드
 * seq: 사용자가 지정한 이미지 순서를 저장할 필드
 * imgPath: S3 버킷에 등록된 이미지 URL을 저장할 필드
 *
 * @author Taejun
 * @version 0.0.1
 */
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update attach set deleted_at = CURRENT_TIMESTAMP where attach_id = ?")
@Entity
public class Attach extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachId;

    @Enumerated(value = EnumType.STRING)
    private AttachType type;

    @Column(nullable = false)
    private Long ids;

    @Column(nullable = false)
    private Long seq;

    @Column(nullable = false)
    private String imgPath;

    @Builder
    public Attach(AttachType type, Long ids, Long seq, String imgPath) {
        this.type = type;
        this.ids = ids;
        this.seq = seq;
        this.imgPath = imgPath;
    }

}

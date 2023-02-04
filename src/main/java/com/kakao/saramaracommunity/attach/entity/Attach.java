package com.kakao.saramaracommunity.attach.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update comment set deleted_at = CURRENT_TIMESTAMP where comment_id = ?")
@Getter
@Entity
public class Attach extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachId;

    @Column(nullable = false)
    private String imgPath;
}

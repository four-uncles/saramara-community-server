package com.kakao.saramaracommunity.member.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

@ToString
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")

@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String profileImage;

    private String token;

    @Builder
    public Member(Type type, String email, String nickname, String password, Role role, String profileImage) {
        this.type = type;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.profileImage = profileImage;
    }
}

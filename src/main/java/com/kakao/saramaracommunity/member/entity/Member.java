package com.kakao.saramaracommunity.member.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@ToString
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "update member set deleted_at = CURRENT_TIMESTAMP where member_id = ?")
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String picture;

    private String token;

    @Builder
    public Member(Type type, String email, String nickname, String password, Role role, String picture) {
        this.type = type;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.picture = picture;
    }

    public Member update(String email, String nickname, String picture) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void changePassword(String password) {
        this.password = password;
    }
    public void changeEmail(String email) {
        this.email = email;
    }
}

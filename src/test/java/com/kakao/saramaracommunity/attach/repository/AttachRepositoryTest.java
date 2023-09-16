package com.kakao.saramaracommunity.attach.repository;

import com.kakao.saramaracommunity.attach.entity.Attach;
import com.kakao.saramaracommunity.attach.entity.AttachType;
import com.kakao.saramaracommunity.attach.repository.AttachRepository;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakao.saramaracommunity.attach.entity.AttachType.BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

/**
 * AttachRepositoryTest: Persistence Layer인 AttachRepository를 테스트할 클래스
 * @DataJpaTest를 통해 JPA Repository 검증을 위한 슬라이스 테스트로 진행한다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Transactional
class AttachRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private AttachRepository attachRepository;

    @DisplayName("원하는 게시글의 이미지 목록을 조회한다.")
    @Test
    void findAllByIds() {
        // given
        Attach attach1 = createAttach(BOARD, 1L, 1L, "test1.jpg");
        Attach attach2 = createAttach(BOARD, 1L, 2L, "test2.jpg");
        Attach attach3 = createAttach(BOARD, 1L, 3L, "test3.jpg");
        attachRepository.saveAll(List.of(attach1, attach2, attach3));

        // when
        List<Attach> attaches = attachRepository.findAllByIds(1L);

        // then
        assertThat(attaches).hasSize(3)
                .extracting("type", "ids", "seq", "imgPath")
                .containsExactlyInAnyOrder(
                        tuple(BOARD, 1L, 1L, "test1.jpg"),
                        tuple(BOARD, 1L, 2L, "test2.jpg"),
                        tuple(BOARD, 1L, 3L, "test3.jpg")
                );
    }

    @DisplayName("게시글의 이미지 목록을 가져올 때, 등록된 이미지가 하나도 없다면 빈 목록을 반환한다.")
    @Test
    void findAllByIdsWhenAttachesIsEmpty() {
        // when
        List<Attach> attaches = attachRepository.findAllByIds(1L);

        // then
        assertThat(attaches).isEmpty();
    }

    private Attach createAttach(AttachType type, Long ids, Long seq, String path) {
        return Attach.builder()
                .type(type)
                .ids(ids)
                .seq(seq)
                .imgPath(path)
                .build();
    }

}
package com.kakao.saramaracommunity.board.entity;

import com.kakao.saramaracommunity.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Entity
@Table(name = "ATTACH_BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attach_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private int imageOrder;

    @Column(nullable = false)
    private String imagePath;

    @Builder
    private AttachBoard(Board board, int imageOrder, String imagePath) {
        this.board = board;
        this.imageOrder = imageOrder;
        this.imagePath = imagePath;
    }

    public void update(int imageOrder, String imagePath) {
        this.imageOrder = imageOrder;
        this.imagePath = imagePath;
    }

    public static List<AttachBoard> createAttachBoards(Board board, List<String> attachPaths) {
        return IntStream.range(0, attachPaths.size())
                .mapToObj(idx -> new AttachBoard(board, idx + 1, attachPaths.get(idx)))
                .collect(Collectors.toList());
    }

    public static List<AttachBoard> updateAttachBoards(Board board, List<String> attachPaths) {
        List<AttachBoard> attachBoards = board.getAttachBoards();
//        if (attachBoards.size() != attachPaths.size()) {
//            throw new BoardAttachOutOfRangeException(ATTACH_BOARD_RANGE_OUT);
//        }
        IntStream.range(0, attachPaths.size())
                .forEach(
                        idx -> attachBoards.get(idx).update(idx + 1, attachPaths.get(idx))
                );
        return attachBoards;
    }

}

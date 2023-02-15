package com.kakao.saramaracommunity.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@Data
public class BoardPageRequestDTO {
    /* 게시글 목록을 저장할 DTO */
    private int page;
    private int size;

    //page와 size 값이 없을 떄 사용할 기본값 설정을 위한 생성자
    @Builder
    public BoardPageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    //page와 size를 가지고 Pageable 객체를 생성해주는 메서드
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }
}

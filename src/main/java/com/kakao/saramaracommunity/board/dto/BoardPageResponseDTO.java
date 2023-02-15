package com.kakao.saramaracommunity.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class BoardPageResponseDTO<DTO, ENTITY> {
    /* 게시글 목록 출력 DTO */
    private List<DTO> dtoList;

    private int totalPage; //전체 페이지 개수
    private int page; //현재 페이지 번호
    private int size; //페이지 당 데이터 출력 개수
    private int start, end; //페이지 시작 번호와 끝 번호
    private boolean prev, next; //이전과 다음 출력 여부
    private List<Integer> pageList; //페이지 번호 목록

    public BoardPageResponseDTO(Page<ENTITY> result, Function<ENTITY, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    //페이지 번호 목록 관련 속성을 결정하는 메서드
    private void makePageList(Pageable pageable) {
        //현재 페이지 번호
        this.page = pageable.getPageNumber()+1;
        //데이터 개수
        this.size=pageable.getPageSize();

        //임시로 마지막 페이지 번호 계산
        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        //시작하는 페이지 번호
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd :totalPage;
        //다음 여부
        next=totalPage>end;
        //페이지 번호 목록 만들기
        pageList = IntStream.rangeClosed(start,end)
                .boxed().collect(Collectors.toList());
    }
}

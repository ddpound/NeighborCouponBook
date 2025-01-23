package com.neighborcouponbook.service;

import com.neighborcouponbook.model.search.BoardSearch;
import com.neighborcouponbook.model.vo.BoardVo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BoardService {

    public ResponseEntity<?> getBoardList(BoardSearch boardSearch);

    public ResponseEntity<?> getBoardDetail(BoardSearch boardSearch);

    ResponseEntity<?> createBoard(BoardVo boardVo);

    ResponseEntity<?> updateBoard(BoardVo boardVo);

    ResponseEntity<?> deleteBoard(Long boardId);
}

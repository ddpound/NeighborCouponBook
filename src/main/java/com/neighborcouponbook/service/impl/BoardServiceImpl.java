package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.model.Board;
import com.neighborcouponbook.model.QBoard;
import com.neighborcouponbook.model.Shop;
import com.neighborcouponbook.model.search.BoardSearch;
import com.neighborcouponbook.model.vo.BoardVo;
import com.neighborcouponbook.repository.BoardRepository;
import com.neighborcouponbook.repository.CouponUserRepository;
import com.neighborcouponbook.service.BoardService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.neighborcouponbook.model.QBoard.board;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardServiceImpl implements BoardService {

    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final CouponUserRepository couponUserRepository;

    @Override
    public ResponseEntity<?> getBoardList(BoardSearch boardSearch) {
        try {
            QBoard board = QBoard.board;

            List<Board> results = queryFactory
                    .select(board)
                    .from(board)
                    .where(createSearchCondition(boardSearch)
                    ,board.isDeleted.eq(false)).fetch();

            // 조회 결과가 없으면 null 반환 또는 예외 처리
            if (results == null || results.isEmpty()) {
                throw new RuntimeException("해당 게시판에 등록된 글이 없습니다.");
            }

            return ResponseUtil.createResponse(new BoardVo().convertToBoardVoList(results), 1 , "게시글 반환이 완료되었습니다.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("selectBoardList ERROR : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private BooleanBuilder createSearchCondition(BoardSearch search) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(search.getBoardId() != null) booleanBuilder.and(board.boardId.eq(search.getBoardId()));
        if(search.getBoardType() != null) booleanBuilder.and(board.boardType.eq(search.getBoardType()));
        if(search.getBoardTitle() != null) booleanBuilder.and(board.boardTitle.contains(search.getBoardTitle()));

        return booleanBuilder;
    }

    @Override
    public ResponseEntity<?> getBoardDetail(BoardSearch boardSearch) {
        try {
            BoardVo boardVo = new BoardVo();

            Board board = boardRepository.findById(boardSearch.getBoardId())
                    .orElseThrow(() -> new IllegalArgumentException());

            return ResponseUtil.createResponse(boardVo.convertToBoardVo(board), 1, "게시글 조회 성공", HttpStatus.OK);
        }catch (Exception e) {
            log.debug("게시글 조회 실패 : "  +e.getMessage());
            return ResponseUtil.createSuccessResponse(-1, "게시글 조회 실패 : " +e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> createBoard(BoardVo boardVo) {
        try {
            if (boardVo == null) {
                return ResponseUtil.createErrorResponse( "입력 데이터가 없습니다.", HttpStatus.NO_CONTENT);
            }

            Board board = new Board();
            board.createBoard(boardVo.getBoardType(), boardVo.getBoardTitle(), boardVo.getBoardContent());
            board.settingCreateData(AuthUtil.getLoginUserData().getUserId());

            boardRepository.save(board);
            return ResponseUtil.createSuccessResponse(1, "게시글 저장 성공 : " + boardVo);
        } catch (Exception e) {
            log.debug("게시글 저장 실패 : "  +e.getMessage());
            return ResponseUtil.createSuccessResponse(-1, "게시글 저장 실패 : " +e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateBoard(BoardVo boardVo) {
        try {
            Board updateBoardInfo = boardRepository.findById(boardVo.getBoardId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

            updateBoardInfo.updateBoard(boardVo.getBoardTitle(),
                    boardVo.getBoardContent());

            updateBoardInfo.updateData(AuthUtil.getLoginUserData().getUserId());

            return ResponseUtil.createSuccessResponse(1, "수정이 완료되었습니다.");
        } catch (Exception e) {
            log.error("Board update failed : ", e);
            return ResponseUtil.createSuccessResponse(-1, "수정에 실패했습니다. : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteBoard(Long boardId) {
        try {
            Board softDeleteBoardInfo = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글 정보가 존재하지 않습니다"));
            softDeleteBoardInfo.softDeleteData(AuthUtil.getLoginUserData().getUserId());

            return ResponseUtil.createSuccessResponse(1, "삭제가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "삭제에 실패했습니다. : " + e.getMessage());
        }
    }
}

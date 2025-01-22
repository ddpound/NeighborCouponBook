package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.Board;
import com.neighborcouponbook.model.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardVo extends CommonColumnVo{
    private Long boardId;
    private String boardType;
    private String boardTitle;
    private String boardContent;
    public BoardVo convertToBoardVo(Board board) {
        BoardVo boardVo = new BoardVo();

        if(!NullChecker.isNull(board.getBoardId())) boardVo.setBoardId(board.getBoardId());
        if(!NullChecker.isNull(board.getBoardType())) boardVo.setBoardType(board.getBoardType());
        if(!NullChecker.isNull(board.getBoardTitle())) boardVo.setBoardTitle(board.getBoardTitle());
        if(!NullChecker.isNull(board.getBoardContent())) boardVo.setBoardContent(board.getBoardContent());
        // 공통 요소들
        if(!NullChecker.isNull(board.returnAllCommonColumn())) boardVo.settingCommonColumnVo(board.returnAllCommonColumn());

        return boardVo;
    }

    public List<BoardVo> convertToBoardVoList(List<Board> boards) {
        List<BoardVo> boardVoList = new ArrayList<>();
        boards.forEach(board -> boardVoList.add(convertToBoardVo(board)));

        return boardVoList;
    }
}

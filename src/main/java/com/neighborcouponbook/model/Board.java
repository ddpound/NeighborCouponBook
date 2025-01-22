package com.neighborcouponbook.model;

import com.neighborcouponbook.common.util.NullChecker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
public class Board extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    // 공지 001 커뮤니티 002
    private String boardType;

    @Column(unique = false, nullable = false)
    private String boardTitle;
    @Column(unique = false, nullable = false)
    private String boardContent;


    public void createBoard(String boardType,
                           String boardTitle,
                           String boardContent) {

        if(!NullChecker.isNull(boardType)) this.boardType = boardType;
        if(!NullChecker.isNull(boardTitle)) this.boardTitle = boardTitle;
        if(!NullChecker.isNull(boardContent)) this.boardContent = boardContent;
    }

    public void updateBoard(String boardTitle, String boardContent) {
        if (boardTitle != null) {
            this.boardTitle = boardTitle;
        }
        if (boardContent != null) {
            this.boardContent = boardContent;
        }
    }

}

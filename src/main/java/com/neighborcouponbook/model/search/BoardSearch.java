package com.neighborcouponbook.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardSearch extends CommonSearch{
    private Long boardId;
    private String boardType;
    private String boardTitle;
}

package com.neighborcouponbook.controller;

import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.Board;
import com.neighborcouponbook.model.search.BoardSearch;
import com.neighborcouponbook.model.vo.BoardVo;
import com.neighborcouponbook.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "board")
@RestController
public class BoardController {

    private final BoardService boardService;

    @Operation(
            summary = "게시판 리스트",
            description = "공지사항, 커뮤니티 게시판 등 리스트",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardSearch.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @GetMapping("/list")
    public ResponseEntity<?> getBoardList(BoardSearch boardSearch) {
        return boardService.getBoardList(boardSearch);
    }

    @Operation(
            summary = "게시글 상세",
            description = "boardSearch - boardId로 게시글 상세 표시",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardSearch.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @GetMapping("/detail")
    public ResponseEntity<?> getBoardDetail(BoardSearch boardSearch) {
        return boardService.getBoardDetail(boardSearch);
    }

    @Operation(
            summary = "게시글 작성",
            description = "공지사항, 커뮤니티 게시판 등, 일반 게시글 작성" +
                    "타입, 제목, 내용 지정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardSearch.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "작성 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @PostMapping("create")
    public ResponseEntity<?> createBoard(@RequestBody BoardVo boardVo) {
        try {
            return boardService.createBoard(boardVo);
        } catch (Exception e) {
            log.error(e);
            return ResponseUtil.createErrorResponse("서버 에러가 발생했습니다.");
        }

    }

    @Operation(
            summary = "게시글 수정",
            description = "공지사항, 커뮤니티 게시판 게시글 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardSearch.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "수정 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @PostMapping("update")
    public ResponseEntity<?> updateBoard(@RequestBody BoardVo boardVo) {
        try {
            return boardService.updateBoard(boardVo);
        } catch (Exception e) {
            log.error(e);
            return ResponseUtil.createErrorResponse("서버 에러가 발생했습니다.");
        }
    }
    @Operation(
            summary = "게시글 삭제",
            description = "공지사항, 커뮤니티 게시판 게시글 삭제",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardSearch.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "삭제 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @PostMapping("delete")
    public ResponseEntity<?> deleteBoard(@RequestBody BoardVo boardVo) {
        try {
            return boardService.deleteBoard(boardVo);
        } catch (Exception e) {
            log.error(e);
            return ResponseUtil.createErrorResponse("서버 에러가 발생했습니다.");
        }
    }

}

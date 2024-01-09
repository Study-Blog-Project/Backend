
package com.study.studyproject.board.controller;

import com.study.studyproject.board.dto.BoardOneResponseDto;
import com.study.studyproject.board.dto.BoardReUpdateRequestDto;
import com.study.studyproject.board.dto.BoardWriteRequestDto;
import com.study.studyproject.board.service.BoardService;
import com.study.studyproject.global.GlobalResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/")
@Tag(name = "게시판 API", description = "게시판 Document")
public class BoardController {

    private final BoardService boardService;

    //글쓰기 수정
    @PatchMapping("member/updateWrite")
    public void updateWriting(@RequestBody BoardReUpdateRequestDto boardReUpdateRequestDto) {

        System.out.println("boardReUpdateRequestDto = " + boardReUpdateRequestDto);
        boardService.updateWrite(boardReUpdateRequestDto);
    }

    //글쓰기 작성
    @PostMapping("member/writing")
    @Operation(summary = "글쓰기 작성",description = "글쓰기 작성합니다.")
    public ResponseEntity<GlobalResultDto> writing(@CookieValue(value = "Refresh_Token") String token, @RequestBody BoardWriteRequestDto boardWriteRequestDto) {
        System.out.println("boardWriteRequestDto = " + boardWriteRequestDto);
        System.out.println("token = " + token);
        GlobalResultDto body = boardService.boardSave(boardWriteRequestDto, token);
        return ResponseEntity.ok(body);
    }

    
    //삭제
    @DeleteMapping("member/{boardId}")
    @Operation(summary = "게시글 삭제 ",description = "게시글을 삭제합니다.")
    public ResponseEntity<GlobalResultDto> deleteBoard(@Parameter(description = "게시판 ID") @PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.boardDeleteOne(boardId));

    }


    //글 조회 1개 - 댓글 기능 (x) -  추후 추가
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardOneResponseDto> writing(@Parameter(description = "게시판 ID") @PathVariable Long boardId) {
        BoardOneResponseDto boardOneResponseDto = boardService.boardOne(boardId);
        return ResponseEntity.ok(boardOneResponseDto);

    }


    }
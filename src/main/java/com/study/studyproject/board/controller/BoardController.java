
package com.study.studyproject.board.controller;

import com.study.studyproject.board.dto.BoardChangeRecruitRequestDto;
import com.study.studyproject.board.dto.BoardOneResponseDto;
import com.study.studyproject.board.dto.BoardReUpdateRequestDto;
import com.study.studyproject.board.dto.BoardWriteRequestDto;
import com.study.studyproject.board.service.BoardService;
import com.study.studyproject.global.GlobalResultDto;
import com.study.studyproject.global.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/")
@Tag(name = "게시판 API", description = "게시판 Document")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    //글쓰기 수정
    @PatchMapping("member/updateWrite")
    @Operation(summary = "글쓰기 수정", description = "글쓰기 수정 기능")
    public ResponseEntity<GlobalResultDto> updateWriting(@RequestBody BoardReUpdateRequestDto boardReUpdateRequestDto) {
        System.out.println("boardReUpdateRequestDto = " + boardReUpdateRequestDto);
        return ResponseEntity.ok(boardService.updateWrite(boardReUpdateRequestDto));

    }

    @PostMapping("member/writing")
    @Operation(summary = "글쓰기 작성", description = "글쓰기 작성")
    public ResponseEntity<GlobalResultDto> writing(@RequestBody BoardWriteRequestDto boardWriteRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)  {
        return ResponseEntity.ok(boardService.boardSave(boardWriteRequestDto, userDetails.getMemberId()));

    }

    @Operation(summary = "모집구분 변경", description = "모집구분 변경")
    @PatchMapping("/member/changeRecruit")
    public ResponseEntity<GlobalResultDto> changeRecruit(@RequestBody BoardChangeRecruitRequestDto boardChangeRecruitRequestDto) {
        return ResponseEntity.ok(boardService.changeRecruit(boardChangeRecruitRequestDto));
    }


    //삭제
    @DeleteMapping("member/delete")
    @Operation(summary = "게시글 삭제 ", description = "해당 게시글 삭제")
    public ResponseEntity<GlobalResultDto> deleteBoard(@Parameter(description = "게시판 ID") @RequestParam(name = "boardId") Long boardId) {
        return ResponseEntity.ok(boardService.boardDeleteOne(boardId));

    }


    //글 조회 1개 -
    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 상세", description = "게시글 상세페이지")
    public ResponseEntity<BoardOneResponseDto> writing(@Parameter(description = "게시판 ID") @PathVariable(name = "boardId") Long boardId
            , @AuthenticationPrincipal UserDetailsImpl userDetails ) {

        log.info("----게시글 상세 들어옴------");
        BoardOneResponseDto boardOneResponseDto = boardService.boardOne(boardId, userDetails.getMemberId());
        return ResponseEntity.ok(boardOneResponseDto);

    }



}

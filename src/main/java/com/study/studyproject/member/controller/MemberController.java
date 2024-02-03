package com.study.studyproject.member.controller;

import com.study.studyproject.global.auth.UserDetailsImpl;
import com.study.studyproject.global.jwt.JwtUtil;
import com.study.studyproject.list.dto.ListResponseDto;
import com.study.studyproject.global.GlobalResultDto;
import com.study.studyproject.member.dto.MemberListRequestDto;
import com.study.studyproject.member.dto.MemberUpdateResDto;
import com.study.studyproject.member.dto.UserInfoResponseDto;
import com.study.studyproject.member.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "마이페이지 기능", description = "사용자 마이페이지 기능 구현 ")
public class MemberController {


    private final MemberServiceImpl memberService;
    private final JwtUtil jwtUtil;

    //사용자 정보 조회
    @Operation(summary = "사용자 정보 조회", description = "자신의 사용자 정보를 조회")
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> userInfo(@RequestHeader("Access_Token") String token) {
        Long idFromToken = jwtUtil.getIdFromToken(jwtUtil.resolveToken(token));
        return ResponseEntity.ok(memberService.userInfoService(idFromToken));
    }

    //수정
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 기능")
    @PatchMapping("/info/update")
    public ResponseEntity<GlobalResultDto> userInfoUpdate(@RequestBody MemberUpdateResDto memberUpdateResDto, @RequestHeader("Access_Token") String token) {
        Long idFromToken = jwtUtil.getIdFromToken(jwtUtil.resolveToken(token));
        return ResponseEntity.ok(memberService.userInfoUpdate(idFromToken, memberUpdateResDto));
    }


    @Operation(summary = "사용자 게시글 조회", description = "사용자 스터디 게시글 조회")
    @GetMapping("/list")
    public ResponseEntity<Page<ListResponseDto>> mainList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
             MemberListRequestDto memberListRequestDto,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(memberService.listMember(userDetails.getMemberId(), memberListRequestDto, pageable));
    }



}

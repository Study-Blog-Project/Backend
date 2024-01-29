package com.study.studyproject.member.controller;

import com.study.studyproject.member.dto.AdminDashBoardResponseDto;
import com.study.studyproject.member.dto.MemberListRequestDto;
import com.study.studyproject.member.dto.UserInfoResponseDto;
import com.study.studyproject.member.service.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "관리자 기능",description = "사용자 전체조회와 관리자 정보 조회")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/userAll")
    @Operation(summary = "전체 사용자 조회", description = "사용자 검색 조회 및 사용자 전체 조회 가능")
    public Page<UserInfoResponseDto> userAllInfo(
            @RequestParam(required = false) String username,
            @PageableDefault(size = 10) Pageable pageable) {

        return adminService.userInfoAll(username,pageable);
    }

    @GetMapping("/dashBoard")
    @Operation(summary = "관리자 대시보드 ", description = "관리자 정보와 게시글 정보 조회")
    public AdminDashBoardResponseDto adminDashInfo(
            @CookieValue(value = "Refresh_Token") String token,
            MemberListRequestDto memberListRequestDto,
                                                                      @PageableDefault(size = 10) Pageable pageable) {

        return adminService.adminDashBoardInfo(token,memberListRequestDto,pageable);
    }
}

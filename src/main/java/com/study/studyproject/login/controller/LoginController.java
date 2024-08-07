package com.study.studyproject.login.controller;

import com.study.studyproject.global.GlobalResultDto;
import com.study.studyproject.global.auth.UserDetailsImpl;
import com.study.studyproject.global.jwt.JwtUtil;
import com.study.studyproject.login.dto.LoginRequest;
import com.study.studyproject.login.dto.LoginResponseDto;
import com.study.studyproject.login.dto.SignRequest;
import com.study.studyproject.login.service.LoginService;
import com.study.studyproject.login.service.LogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@Tag(name = "로그인/로그아웃/회원가입 기능",description = "사용자의 로그인과 로그아웃 회원가입 기능")
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final LogoutService logoutService;
    private final JwtUtil jwtUtil;

    //회원가입
    @Operation(summary = "회원가입", description = "사용자 회원가입")
    @PostMapping("/sign")
    public ResponseEntity<GlobalResultDto> sign(@Validated @RequestBody SignRequest signRequest) {
        return ResponseEntity.ok(loginService.sign(signRequest));

    }


    @Operation(summary = "로그인", description = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(loginService.loginService(loginRequest, response));

    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
    @PostMapping("/service-logout")
    public ResponseEntity<GlobalResultDto> logout(@RequestHeader("Access_Token") String token) {
        return ResponseEntity.ok(logoutService.logoutService(token));
    }

}

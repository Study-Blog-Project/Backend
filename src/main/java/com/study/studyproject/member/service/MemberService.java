package com.study.studyproject.member.service;


import com.study.studyproject.list.dto.ListResponseDto;
import com.study.studyproject.global.GlobalResultDto;
import com.study.studyproject.member.dto.MemberListRequestDto;
import com.study.studyproject.member.dto.MemberUpdateResDto;
import com.study.studyproject.member.dto.UserInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {


    //사용자 정보 조회
    UserInfoResponseDto userInfoService(String token);


    //수정
    GlobalResultDto userInfoUpdate(String token, MemberUpdateResDto memberUpdateResDto);

    Page<ListResponseDto> listMember(String token, MemberListRequestDto memberListRequestDto, Pageable pageable);
    //




}

package com.study.studyproject.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    ETC("기타"),
    ALL("전체"),
    CS("CS"),
    CODE_TEST("코테"),
    PROJECT("프로젝트");
    private final String text;


}

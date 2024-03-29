package com.study.studyproject.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public static boolean containsRoleType(Role type) {
        return List.of(ROLE_USER, ROLE_ADMIN).contains(type);
    }

}

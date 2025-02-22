package com.gautama.abscencerecordhitsbackend.api.enums;

import lombok.Getter;

@Getter
public enum UserQueryType {
    ALL("Все пользователи"),
    TEACHERS("Только преподаватели"),
    STUDENTS("Только студенты"),
    WITHOUT_ROLE("Только без роли");

    private final String displayName;

    UserQueryType(String displayName) {
        this.displayName = displayName;
    }
}
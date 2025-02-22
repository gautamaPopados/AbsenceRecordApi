package com.gautama.abscencerecordhitsbackend.api.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    DEANERY("Деканат"),
    TEACHER("Преподаватель"),
    STUDENT("Студент");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}

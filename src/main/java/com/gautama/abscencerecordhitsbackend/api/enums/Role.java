package com.gautama.abscencerecordhitsbackend.api.enums;

import lombok.Getter;

@Getter
public enum Role {
    DEANERY("Деканат"),
    TEACHER("Преподаватель"),
    STUDENT("Студент"),
    USER("Пользователь"),
    ADMIN("Админ");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

}

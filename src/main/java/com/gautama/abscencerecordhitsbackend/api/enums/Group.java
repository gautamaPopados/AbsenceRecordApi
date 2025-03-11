package com.gautama.abscencerecordhitsbackend.api.enums;

import lombok.Getter;

@Getter
public enum Group {
    GROUP_972301("972301"),
    GROUP_972302("972302"),
    GROUP_972303("972303");

    private final String groupNumber;

    Group(String groupNumber) {
        this.groupNumber = groupNumber;
    }
}

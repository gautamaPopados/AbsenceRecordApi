package com.gautama.abscencerecordhitsbackend.api.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    REJECTED("Отклонена"),
    ACCEPTED("Подтверждена"),
    PENDING("На проверке");

    private final String statusName;

    RequestStatus(String statusName) {
        this.statusName = statusName;
    }
}

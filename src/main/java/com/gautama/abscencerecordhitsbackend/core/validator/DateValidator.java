package com.gautama.abscencerecordhitsbackend.core.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateValidator {
    public boolean checkDate(LocalDate currentDate, LocalDate newDate) {
        return newDate.isAfter(currentDate);
    }
}

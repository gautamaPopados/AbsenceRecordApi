package com.gautama.abscencerecordhitsbackend.core.converter;

import jakarta.persistence.Converter;
import jakarta.persistence.AttributeConverter;
import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return attribute == null ? null : Date.from(attribute.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return dbData == null ? null : dbData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

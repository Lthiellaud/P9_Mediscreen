package com.mediscreen.massessment.utils;

import java.time.LocalDate;
import java.time.Period;

public final class DateUtil {

    /* Constructeur privé */
    private DateUtil() {

    }

    public static Integer getAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }
}

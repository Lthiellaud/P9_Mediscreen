package com.mediscreen.massessment.utils;

import com.mediscreen.massessment.model.Triggers;
import com.mediscreen.massessment.model.constant.KeyWords;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AssessmentUtil {

    /* Constructeur privé */
    private AssessmentUtil() {

    }

    public static Triggers initTriggers() {
        Triggers triggers = new Triggers();
        triggers.setTriggers(Stream.of(KeyWords.values())
                .map(KeyWords::getValue)
                .collect(Collectors.toList()));

        return triggers;
    }
}

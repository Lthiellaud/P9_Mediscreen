package com.mediscreen.massessment.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

    @Test
    public void getAgeTest() {
        LocalDate birthDate = LocalDate.now().minusYears(40);
        assertThat(DateUtil.getAge(birthDate)).isEqualTo(40);
        birthDate = birthDate.plusDays(1);
        assertThat(DateUtil.getAge(birthDate)).isEqualTo(39);
    }

}
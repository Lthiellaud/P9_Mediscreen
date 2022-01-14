package com.mediscreen.massessment.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssessmentUtilTest {

    @Test
    public void initTriggersTest() {
        List<String> triggers = Arrays.asList("Hémoglobine A1C", "Microalbumine", "Taille", "Poids", "Fumeur",
                "Anormal", "Cholestérol", "Vertige", "Rechute", "Réaction", "Anticorps");
        assertThat(AssessmentUtil.initTriggers().getTriggers()).isEqualTo(triggers);
    }

}
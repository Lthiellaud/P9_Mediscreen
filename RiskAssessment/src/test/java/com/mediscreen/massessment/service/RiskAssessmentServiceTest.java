package com.mediscreen.massessment.service;

import com.mediscreen.massessment.model.Patient;
import com.mediscreen.massessment.model.Triggers;
import com.mediscreen.massessment.proxies.PatientManagementProxy;
import com.mediscreen.massessment.proxies.PatientNotesProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RiskAssessmentServiceTest {

    @MockBean
    private PatientManagementProxy patientManagementProxy;

    @MockBean
    private PatientNotesProxy patientNotesProxy;

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    private static Patient patientToAssess = new Patient(1, "Lucas","Ferguson"
            , LocalDate.of(1980, 1, 1), "M", "2 Warren Street ", "387-866-1399");

    private static LocalDate today = LocalDate.now();

    @Test
    void assessRiskLevelNoneTest() {
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(0L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("None");
    }

    @Test
    void assessRiskLevelBorderlineTest() {
        patientToAssess.setBirthDate(today.minusYears(30));
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(2L);

        System.out.println(patientToAssess.getSex());
        System.out.println(patientToAssess.getBirthDate());
        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("Borderline");
    }

    @Test
    void assessRiskLevelInDangerUnder30M3Test() {
        patientToAssess.setBirthDate(today.minusYears(29));
        patientToAssess.setSex("M");
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(3L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("In Danger");
    }

    @Test
    void assessRiskLevelInDangerUnder30F4Test() {
        patientToAssess.setBirthDate(today.minusYears(29));
        patientToAssess.setSex("F");
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(4L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("In Danger");
    }

    @Test
    void assessRiskLevelInDangerAbove30M6Test() {
        patientToAssess.setBirthDate(today.minusYears(33));
        patientToAssess.setSex("M");
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(6L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("In Danger");
    }

    @Test
    void assessRiskLevelEarlyOnsetUnder30M5Test() {
        patientToAssess.setBirthDate(today.minusYears(29));
        patientToAssess.setSex("M");
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(5L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("Early onset");
    }

    @Test
    void assessRiskLevelEarlyOnsetUnder30F7Test() {
        patientToAssess.setBirthDate(today.minusYears(29));
        patientToAssess.setSex("F");
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(7L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("Early onset");
    }

    @Test
    void assessRiskLevelEarlyOnsetAbove30F8Test() {
        patientToAssess.setBirthDate(today.minusYears(33));
        when(patientManagementProxy.getPatientById(1)).thenReturn(patientToAssess);
        when(patientNotesProxy.countNotesByPatientIdWithTrigger(any(), any(Triggers.class))).thenReturn(8L);

        assertThat(riskAssessmentService.assessRiskLevel(1)).isEqualTo("Early onset");
    }

}
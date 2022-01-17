package com.mediscreen.massessment.service.implementation;

import com.mediscreen.massessment.model.Patient;
import com.mediscreen.massessment.model.constant.PivotAge;
import com.mediscreen.massessment.model.constant.RiskLevel;
import com.mediscreen.massessment.proxies.PatientManagementProxy;
import com.mediscreen.massessment.proxies.PatientNotesProxy;
import com.mediscreen.massessment.service.RiskAssessmentService;
import com.mediscreen.massessment.utils.AssessmentUtil;
import com.mediscreen.massessment.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Evaluation du risque de développer un diabète par patient.
 */
@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    @Autowired
    private PatientNotesProxy patientNotesProxy;

    @Autowired
    private PatientManagementProxy patientManagementProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskAssessmentServiceImpl.class);

    /**
     * Renvoi le niveau de risque d'un patient.
     * Le calcul est fait en fonction de son age, son sexe et de la présence
     * @param patientId l'Id du patient à évaluer
     * @return le niveau de risque du patient
     */
    @Override
    public String assessRiskLevel(Integer patientId) {
        LOGGER.info("Risque évalué pour le patient id " + patientId);
        Patient patient = patientManagementProxy.getPatientById(patientId);
        Long keyWordsNumber = patientNotesProxy.countNotesByPatientIdWithTrigger(patientId, AssessmentUtil.initTriggers());
        return applyAssessmentAlgorithm(keyWordsNumber, patient.getSex(), DateUtil.getAge(patient.getBirthDate()));

    }

    private String applyAssessmentAlgorithm(Long keyWordNumber, String sex, Integer age) {
        boolean isLessThanPivotAge = age < PivotAge.PIVOT_AGE;
        boolean isWoman = "F".equals(sex);

        //RiskLevel = None
        if (keyWordNumber <= 1 ) {
                return RiskLevel.NONE.getValue();
        }
        if (isLessThanPivotAge && (keyWordNumber == 2 || (keyWordNumber == 3 && isWoman))) {
            return RiskLevel.NONE.getValue();
        }

        //RiskLevel = Borderline
        if (!isLessThanPivotAge && keyWordNumber <= 5 ) {
            return RiskLevel.BORDERLINE.getValue();
        }

        //RiskLevel = In danger
        if (isLessThanPivotAge && ((keyWordNumber <= 6 && isWoman) || (keyWordNumber <= 4 ))) {
            return RiskLevel.IN_DANGER.getValue();
        }
        if (!isLessThanPivotAge && keyWordNumber <= 7) {
            return RiskLevel.IN_DANGER.getValue();
        }

        //RiskLevel = Early onset
        return RiskLevel.EARLY_ONSET.getValue();
    }
}

package com.mediscreen.webapp.controller;

import com.mediscreen.webapp.exception.NotFoundException;
import com.mediscreen.webapp.mapper.AssessmentDTOMapper;
import com.mediscreen.webapp.model.Patient;
import com.mediscreen.webapp.proxies.PatientManagementProxy;
import com.mediscreen.webapp.proxies.RiskAssessmentProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RiskAssessmentController {

    @Autowired
    private PatientManagementProxy patientManagementProxy;

    @Autowired
    private RiskAssessmentProxy riskAssessmentProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(RiskAssessmentController.class);

    @GetMapping("/assessment/{id}")
    public String showPatientAssessment(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        try {
            Patient patient = patientManagementProxy.getPatientById(id);
            String assessment = riskAssessmentProxy.getRiskAssessment(id);
            model.addAttribute("assessmentDTO", AssessmentDTOMapper.INSTANCE.from(patient, assessment));
            return "assessment/get";
        } catch (NotFoundException e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Problème lors de la récupération des données " + e.toString());
            attributes.addFlashAttribute("messageType", "error");
            attributes.addFlashAttribute("message", "Problème lors de la récupération des données, Merci de réessayer plus tard");
        }
        return "redirect:/patient/list";

    }

}

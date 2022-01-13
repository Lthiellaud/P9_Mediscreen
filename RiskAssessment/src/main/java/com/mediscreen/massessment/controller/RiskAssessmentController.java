package com.mediscreen.massessment.controller;

import com.mediscreen.massessment.service.RiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiskAssessmentController {

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    @GetMapping("/assessment/{id}")
    public String getRiskAssessment(@PathVariable Integer id) {
        return riskAssessmentService.assessRiskLevel(id);
    }
}

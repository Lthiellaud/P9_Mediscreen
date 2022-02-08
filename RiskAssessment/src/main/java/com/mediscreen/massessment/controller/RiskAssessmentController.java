package com.mediscreen.massessment.controller;

import com.mediscreen.massessment.service.RiskAssessmentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api("API Evaluation du risque de développer un diabète")
@RestController
public class RiskAssessmentController {

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    @ApiOperation(value = "Evaluation du risque de développer un diabète pour un patient")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "patientId {id} non trouvé")})
    @GetMapping("/assessment/{id}")
    public String getRiskAssessment(@ApiParam(value = "Id du patient") @PathVariable Integer id) {
        return riskAssessmentService.assessRiskLevel(id);
    }
}

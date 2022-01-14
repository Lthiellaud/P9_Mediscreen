package com.mediscreen.webapp.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "RiskAssessment", url = "${feign.url.assessment}")
public interface RiskAssessmentProxy {

    @GetMapping("/assessment/{id}")
    String getRiskAssessment(@PathVariable Integer id);

}

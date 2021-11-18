package com.mediscreen.webapp.integration;

import com.mediscreen.webapp.proxies.PatientManagementProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PatientManagementProxiesIT {

    @Autowired
    PatientManagementProxy patientManagementProxy;

    @Test
    public void badRequestTest() {

    }



}

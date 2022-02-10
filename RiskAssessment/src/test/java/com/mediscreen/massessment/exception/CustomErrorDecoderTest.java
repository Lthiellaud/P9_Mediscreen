package com.mediscreen.massessment.exception;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CustomErrorDecoderTest {

    Map<String, Collection<String>> headers = new LinkedHashMap<>();
    CustomErrorDecoder customErrorDecoder = new CustomErrorDecoder();

    @Test
    void decode404Test() {
        RequestTemplate requestTemplate = new RequestTemplate();
        requestTemplate.body("Le body");

        String body = "{\n" +
                "  \"timestamp\" : \"2022-02-10T11:09:33.342+00:00\",\n" +
                "  \"status\" : 404,\n" +
                "  \"error\" : \"Not Found\",\n" +
                "  \"message\" : \"patientId 200 non trouvé\",\n" +
                "  \"path\" : \"/patient/patientById/200\"\n" +
                "}\n";


        Response response = Response.builder()
                .status(404)
                .reason("")
                .request(Request.create(Request.HttpMethod.GET, "http://localhost:8081/patient/patientById/300"
                        , headers, requestTemplate.body(), StandardCharsets.UTF_8, requestTemplate))
                .headers(headers)
                .body(body, StandardCharsets.UTF_8)
                .build();

        Exception exception = customErrorDecoder.decode("PatientManagementProxy#getPatientById(Integer)", response);
        assertThat(exception.getMessage()).isEqualTo("patientId 200 non trouvé");
        assertThat(exception).isInstanceOf(NotFoundException.class);

    }

    @Test
    void decode500Test() {
        RequestTemplate requestTemplate = new RequestTemplate();
        requestTemplate.body("Le body");

        String body = "{\n" +
                "  \"timestamp\" : \"2022-02-10T11:09:33.342+00:00\",\n" +
                "  \"status\" : 410,\n" +
                "  \"error\" : \"Conflict\",\n" +
                "  \"message\" : \"Un patient existe déjà avec ces mêmes données clé (patientId 20)\",\n" +
                "  \"path\" : \"/patient/add?family=Pat&given=Mat&dob=2020-04-15&sex=M&address&phone\"\n" +
                "}\n";


        Response response = Response.builder()
                .status(500)
                .reason("")
                .request(Request.create(Request.HttpMethod.GET
                        , "http://localhost:8081/patient/add?family=Pat&given=Mat&dob=2020-04-15&sex=M&address&phone"
                        , headers, requestTemplate.body(), StandardCharsets.UTF_8, requestTemplate))
                .headers(headers)
                .body(body, StandardCharsets.UTF_8)
                .build();

        Exception exception = customErrorDecoder.decode("PatientManagementProxy#getPatientById(Integer)", response);
        assertThat(exception.getMessage()).contains("[500");


    }

}
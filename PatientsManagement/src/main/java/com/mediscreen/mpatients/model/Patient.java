package com.mediscreen.mpatients.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="patient")
public class Patient implements Serializable {

    @ApiModelProperty(value = "Id du patient")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    @ApiModelProperty(value = "Prénom du patient", required = true, example = "Test", position = 1)
    @NotNull
    private String firstName;
    @ApiModelProperty(value = "Nom de famille du patient",required = true, example = "TestNone", position = 2)
    @NotNull
    private String lastName;
    @ApiModelProperty(value = "Date de naissance du patient", required = true, example = "1966-12-31", position = 3)
    @NotNull
    private LocalDate birthDate;
    @ApiModelProperty(value = "Genre (M ou F)", required = true, example = "F", position = 4)
    @NotNull
    private String sex;
    @ApiModelProperty(value = "Adresse postale du patient", example = "1 Brookside St", allowEmptyValue = true, position = 5)
    private String homeAddress;
    @ApiModelProperty(value = "Numéro de téléphone du patient", example = "100-222-3333", allowEmptyValue = true, position = 6)
    private String phoneNumber;

    public Patient(String firstName, String lastName, LocalDate birthDate, String sex, String homeAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.homeAddress = homeAddress;
        this.phoneNumber = phoneNumber;
    }



}

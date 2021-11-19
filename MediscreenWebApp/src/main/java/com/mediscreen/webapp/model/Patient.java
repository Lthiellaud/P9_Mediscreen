package com.mediscreen.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Integer patientId;
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date ne peut être dans le futur")
    private LocalDate birthDate;
    @Pattern(regexp = "[MF]",message = "Saisir M ou F")
    private String sex;
    private String homeAddress;
    private String phoneNumber;

}

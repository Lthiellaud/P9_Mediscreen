package com.mediscreen.mpatients.service.implementation;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.exception.NotFoundException;
import com.mediscreen.mpatients.model.DTO.PatientDTO;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import com.mediscreen.mpatients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gestion des données administratives des patients
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatient() {
        return patientRepository.findAll();
    }

    /**
     * Pour trouver un patient par son id
     * @param id id
     * @return le patient s'il a été trouvé / lance une exception sinon
     */
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient Id " + id + " non trouvé "));
    }

    /**
     * Pour trouver un patient par son nom de famille
     * @param lastName nom
     * @return la liste de patient
     */
    @Override
    public List<Patient> getPatientByLastName(String lastName) {
        return patientRepository.findByLastName(lastName);
    }

    @Override
    public Patient createPatient(String lastName, String firstName, LocalDate birthDate, String sex, String address, String phone) {

        return createPatientDTO(new PatientDTO(lastName, firstName, birthDate, sex, address, phone));

    }

    /**
     * Pour créer un patient / lance une exception si le patient existe déjà
     * @param patientDTO les données du patient
     * @return Le patient créé
     */
    @Override
    public Patient createPatientDTO(PatientDTO patientDTO) {
        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(patientDTO.getFirstName(), patientDTO.getLastName()
                        , patientDTO.getBirthDate(), patientDTO.getSex());

        if (savedPatient.isPresent()) {
            throw new AlreadyExistException("Le patient " + patientDTO.getFirstName() + " " + patientDTO.getLastName()
                    + ", né le " + patientDTO.getBirthDate() + " - sexe " + patientDTO.getSex() + " - existe déjà. Id : "
                    + savedPatient.get().getPatientId());
        }
        return patientRepository.save(fromDtoToPatient(patientDTO) );

    }

    private static Patient fromDtoToPatient(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setFirstName(patientDTO.getFirstName().trim());
        patient.setLastName(patientDTO.getLastName().trim());
        patient.setBirthDate(patientDTO.getBirthDate());
        patient.setSex(patientDTO.getSex().trim());
        patient.setHomeAddress(patientDTO.getHomeAddress().trim());
        patient.setPhoneNumber(patientDTO.getPhoneNumber().trim());
        return patient;
    }
    /**
     * Pour mettre à jour les données administratives d'un patient
     * @param patientDTO les données du patient
     * @return Patient mis à jour
     */
    @Override
    public Patient updatePatient(PatientDTO patientDTO, Long id) {
        //Vérifie si la clé unique est modifiée et les nouvelles données ne sont pas déjà utilisées
        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(patientDTO.getFirstName(), patientDTO.getLastName()
                        , patientDTO.getBirthDate(), patientDTO.getSex());

        if (savedPatient.isPresent() && savedPatient.get().getPatientId() != id) {
            throw new AlreadyExistException("Le patient " + patientDTO.getFirstName() + " " + patientDTO.getLastName()
                    + ", né le " + patientDTO.getBirthDate() + " - sexe " + patientDTO.getSex()
                    + " - existe déjà sous un id différent. Id à mettre à jour : " + id
                    + " / id en base : " + savedPatient.get().getPatientId());
        }

        Patient patient = fromDtoToPatient(patientDTO);
        patient.setPatientId(id);

        return patientRepository.save(patient);
    }

    /**
     * Pour supprimer un patient identifié par son id/ lance une exception si le patient n'a pas été trouvé
     * @param id id
     */
    @Override
    public void deletePatient(Long id) {
        patientRepository.delete(getPatientById(id));
    }
}

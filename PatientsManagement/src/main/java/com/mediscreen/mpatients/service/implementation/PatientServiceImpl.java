package com.mediscreen.mpatients.service.implementation;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.exception.NotFoundException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import com.mediscreen.mpatients.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Gestion des données administratives des patients
 */
@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);

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
    public Patient getPatientById(Integer id) {
        LOGGER.info("Recherche patient id " + id);
        return patientRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Patient non trouvé");
                    return new NotFoundException("patientId " + id + " non trouvé");
                });
    }

    /**
     * Pour trouver un patient par son nom de famille
     * @param lastName nom
     * @return la liste de patient
     */
    @Override
    public List<Patient> getPatientByLastName(String lastName) {
        LOGGER.info("Recherche patient " + lastName);
        return patientRepository.findByLastName(lastName);
    }

    /**
     * Pour créer un patient / lance une exception si le patient existe déjà
     * @param lastName Nom
     * @param firstName Prénom
     * @param birthDate Date de naissance
     * @param sex genre
     * @param address Adresse
     * @param phone Téléphone
     * @return Le patient créé
     */
    @Override
    public Patient createPatient(String lastName, String firstName, LocalDate birthDate, String sex, String address, String phone) {

        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(firstName.trim(), lastName.trim()
                        , birthDate, sex.trim());
        if (savedPatient.isPresent()) {
            LOGGER.error("Patient à créer déjà en base - id : " + savedPatient.get().getPatientId());
            throw new AlreadyExistException("Un patient existe déjà avec ces mêmes données clé (patientId " + savedPatient.get().getPatientId()+ ")");
        }
        Patient patient = new Patient(firstName.trim(), lastName.trim(), birthDate, sex.trim()
                , address  == null ? null : address.trim(), phone  == null ? null : phone.trim());
        return patientRepository.save(patient);

    }

    /**
     * Pour mettre à jour les données administratives d'un patient
     * @param patient les données du patient
     * @return Patient mis à jour
     */
    @Override
    public Patient updatePatient(Patient patient) {
        //Vérifie si le patient existe bien (lance une exception AlreadyExistException sinon)
        getPatientById(patient.getPatientId());

        //Vérifie si la clé unique est modifiée et les nouvelles données ne sont pas déjà utilisées
        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(patient.getFirstName().trim(), patient.getLastName().trim()
                        , patient.getBirthDate(), patient.getSex().trim());

        if (savedPatient.isPresent() && !savedPatient.get().getPatientId().equals(patient.getPatientId())) {
            LOGGER.error("Patient à mettre à jour (id: "+ patient.getPatientId() + ") existe déjà en base sou sun autre id - id : " + savedPatient.get().getPatientId());
            throw new AlreadyExistException("Un patient existe déjà avec ces mêmes données clé (patientId " + savedPatient.get().getPatientId()+ ")");
        }

        return patientRepository.save(cleanPatient(patient));
    }

    /**
     * Applique un trim() sur les données "String" du patient
     * @param patient le patient à traiter
     * @return le patient sans blanc inutile
     */
    private Patient cleanPatient(Patient patient) {
        return new Patient (patient.getPatientId(), patient.getFirstName().trim(), patient.getLastName().trim(), patient.getBirthDate()
                , patient.getSex().trim(), patient.getHomeAddress() == null ? null : patient.getHomeAddress().trim()
                , patient.getPhoneNumber() == null ? null : patient.getPhoneNumber().trim());
    }
    /**
     * Pour supprimer un patient identifié par son id/ lance une exception si le patient n'a pas été trouvé
     * @param id id
     */
    @Override
    public void deletePatient(Integer id) {
        patientRepository.delete(getPatientById(id));
    }

}

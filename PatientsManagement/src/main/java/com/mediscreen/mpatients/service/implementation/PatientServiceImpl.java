package com.mediscreen.mpatients.service.implementation;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.exception.NotFoundException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import com.mediscreen.mpatients.service.PatientService;
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
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("patientId " + id + " non trouvé"));
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
            throw new AlreadyExistException("Un patient existe déjà avec ces mêmes données clé (patientId " + savedPatient.get().getPatientId()+ ")");
        }
        Patient patient = new Patient();
        patient.setFirstName(firstName.trim());
        patient.setLastName(lastName.trim());
        patient.setBirthDate(birthDate);
        patient.setSex(sex.trim());
        patient.setHomeAddress(address.trim());
        patient.setPhoneNumber(phone.trim());
        return patientRepository.save(patient);

    }

    /**
     * Pour mettre à jour les données administratives d'un patient
     * @param patient les données du patient
     * @return Patient mis à jour
     */
    @Override
    public Patient updatePatient(Patient patient) {
        //Vérifie si le patient existe bien (lance une exception sinon)
        getPatientById(patient.getPatientId());

        //Vérifie si la clé unique est modifiée et les nouvelles données ne sont pas déjà utilisées
        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(patient.getFirstName(), patient.getLastName()
                        , patient.getBirthDate(), patient.getSex());

        if (savedPatient.isPresent() && !savedPatient.get().getPatientId().equals(patient.getPatientId())) {
            throw new AlreadyExistException("Un patient existe déjà avec ces mêmes données clé (patientId " + savedPatient.get().getPatientId()+ ")");
        }

        return patientRepository.save(patient);
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

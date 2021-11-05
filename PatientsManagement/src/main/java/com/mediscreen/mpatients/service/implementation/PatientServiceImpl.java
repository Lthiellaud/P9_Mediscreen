package com.mediscreen.mpatients.service.implementation;

import com.mediscreen.mpatients.exception.AlreadyExistException;
import com.mediscreen.mpatients.exception.NotFoundException;
import com.mediscreen.mpatients.model.Patient;
import com.mediscreen.mpatients.repository.PatientRepository;
import com.mediscreen.mpatients.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param id
     * @return le patient s'il a été trouvé / lance une exception sinon
     */
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient Id " + id + " non trouvé "));
    }

    /**
     * Pour créer un patient / lance une exception si le patient existe déjà
     * @param patient
     */
    @Override
    public void createPatient(Patient patient) {
        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(patient.getFirstName(), patient.getLastName()
                        , patient.getBirthDate(), patient.getSex());

        if (savedPatient.isPresent()) {
            throw new AlreadyExistException("Le patient " + patient.getFirstName() + " " + patient.getLastName()
                    + ", né le " + patient.getBirthDate() + " - sexe " + patient.getSex() + " - existe déjà. Id : "
                    + savedPatient.get().getPatientId());
        }
        patientRepository.save(patient);

    }

    /**
     * Pour mettre à jour les données administratives d'un patient
     * @param patient
     */
    @Override
    public void updatePatient(Patient patient) {
        //Vérifie si la clé unique est modifiée et les nouvelles données ne sont pas déjà utilisées
        Optional<Patient> savedPatient = patientRepository
                .findPatientByFirstNameAndLastNameAndBirthDateAndSex(patient.getFirstName(), patient.getLastName()
                        , patient.getBirthDate(), patient.getSex());

        if (savedPatient.isPresent() && savedPatient.get().getPatientId() != patient.getPatientId()) {
            throw new AlreadyExistException("Le patient " + patient.getFirstName() + " " + patient.getLastName()
                    + ", né le " + patient.getBirthDate() + " - sexe " + patient.getSex()
                    + " - existe déjà sous un id différent. Id à mettre à jour : " + patient.getPatientId()
                    + " / id en base : " + savedPatient.get().getPatientId());
        }

        patientRepository.save(patient);
    }

    /**
     * Pour supprimer un patient identifié par son id/ lance une exception si le patient n'a pas été trouvé
     * @param id
     */
    @Override
    public void deletePatient(Long id) {
        patientRepository.delete(getPatientById(id));
    }
}

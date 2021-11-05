package com.mediscreen.mpatients.repository;


import com.mediscreen.mpatients.model.Appointment;
import com.mediscreen.mpatients.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}

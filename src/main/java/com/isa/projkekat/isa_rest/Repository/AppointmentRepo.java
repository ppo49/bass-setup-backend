package com.isa.projkekat.isa_rest.Repository;

import com.isa.projkekat.isa_rest.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    // Novi Query sa JOIN FETCH za eager loading servisa i korisnika
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.service s LEFT JOIN FETCH a.user u WHERE u.id = :userId")
    List<Appointment> findByUserId(Long userId);

}
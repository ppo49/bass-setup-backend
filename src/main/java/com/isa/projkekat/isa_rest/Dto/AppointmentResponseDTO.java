package com.isa.projkekat.isa_rest.Dto;

import com.isa.projkekat.isa_rest.Model.Appointment;
import com.isa.projkekat.isa_rest.Model.Service;
import com.isa.projkekat.isa_rest.Model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private ServiceResponseDTO service;
    private LocalDateTime dateTime;
    private String status;

    // Konstruktor za mapiranje iz Appointment entiteta
    public AppointmentResponseDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.userId = appointment.getUser().getId();
        this.userEmail = appointment.getUser().getEmail(); // Pristupi user objektu
        this.service = new ServiceResponseDTO(appointment.getService()); // Kreiraj ServiceResponseDTO iz Service entiteta
        this.dateTime = appointment.getDateTime();
        this.status = "PLAĆENO";
    }
}
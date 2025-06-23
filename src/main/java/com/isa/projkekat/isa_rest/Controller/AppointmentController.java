package com.isa.projkekat.isa_rest.Controller;

import com.isa.projkekat.isa_rest.Dto.AppointmentResponseDTO;
import com.isa.projkekat.isa_rest.Dto.ServiceResponseDTO;
import com.isa.projkekat.isa_rest.Dto.UserResponseDTO;
import com.isa.projkekat.isa_rest.Model.Appointment;
import com.isa.projkekat.isa_rest.Model.Service;
import com.isa.projkekat.isa_rest.Model.User;
import com.isa.projkekat.isa_rest.Repository.AppointmentRepo;
import com.isa.projkekat.isa_rest.Repository.ServiceRepo;
import com.isa.projkekat.isa_rest.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @GetMapping
    public List<AppointmentResponseDTO> getAppointments(){
        // Mapiraj entitete u DTO-ove pre slanja
        return appointmentRepo.findAll().stream()
                .map(AppointmentResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id){
        Optional<Appointment> appointment = appointmentRepo.findById(id);
        return appointment.map(app -> ResponseEntity.ok(new AppointmentResponseDTO(app))) // Mapiraj u DTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByUserId(@PathVariable Long userId){
        List<Appointment> appointments = appointmentRepo.findByUserId(userId);

        // Mapiraj listu Appointment entiteta u listu AppointmentResponseDTO
        List<AppointmentResponseDTO> dtos = appointments.stream()
                .map(appointment -> new AppointmentResponseDTO(appointment))
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> saveAppointment(@RequestBody Appointment appointment){
        if (appointment.getUser() == null || appointment.getUser().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (appointment.getService() == null || appointment.getService().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepo.findById(appointment.getUser().getId());
        Optional<Service> serviceOptional = serviceRepo.findById(appointment.getService().getId());

        if (userOptional.isEmpty() || serviceOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointment.setUser(userOptional.get());
        appointment.setService(serviceOptional.get());

        if (appointment.getDateTime() == null) {
            appointment.setDateTime(LocalDateTime.now());
        }

        Appointment savedAppointment = appointmentRepo.save(appointment);
        return new ResponseEntity<>(new AppointmentResponseDTO(savedAppointment), HttpStatus.CREATED); // Vrati DTO
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<AppointmentResponseDTO> markAppointmentAsPaid(@PathVariable Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepo.findById(id);
        if (appointmentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Appointment appointment = appointmentOptional.get();

        return new ResponseEntity<>(new AppointmentResponseDTO(appointment), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointmentDetails){
        Optional<Appointment> existingAppointmentOptional = appointmentRepo.findById(id);
        if (existingAppointmentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Appointment existingAppointment = existingAppointmentOptional.get();
        if (appointmentDetails.getUser() != null && appointmentDetails.getUser().getId() != null) {
            Optional<User> userOptional = userRepo.findById(appointmentDetails.getUser().getId());
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            existingAppointment.setUser(userOptional.get());
        }

        if (appointmentDetails.getService() != null && appointmentDetails.getService().getId() != null) {
            Optional<Service> serviceOptional = serviceRepo.findById(appointmentDetails.getService().getId());
            if (serviceOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            existingAppointment.setService(serviceOptional.get());
        }

        if (appointmentDetails.getDateTime() != null) {
            existingAppointment.setDateTime(appointmentDetails.getDateTime());
        }

        Appointment updatedAppointment = appointmentRepo.save(existingAppointment);
        return new ResponseEntity<>(new AppointmentResponseDTO(updatedAppointment), HttpStatus.OK); // Vrati DTO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id){
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            return new ResponseEntity<>("Appointment deleted successfully with ID: " + id, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Appointment with ID " + id + " not found.", HttpStatus.NOT_FOUND);
    }
}
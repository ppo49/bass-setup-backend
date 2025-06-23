package com.isa.projkekat.isa_rest.Controller;

import com.isa.projkekat.isa_rest.Model.Service;
import com.isa.projkekat.isa_rest.Repository.ServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Dodato za http greske
import org.springframework.dao.DataIntegrityViolationException; // Dodato za sql greske

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceRepo serviceRepo;

    @GetMapping
    public ResponseEntity<List<Service>> getServices(){
        return ResponseEntity.ok(serviceRepo.findAll()); // Vraca 200 OK
    }

    @PostMapping
    public ResponseEntity<Service> saveService(@RequestBody Service service){
        if (serviceRepo.findByName(service.getName()).isPresent()) {
            // Umesto stringa, baci izuzetak koji ce GlobalExceptionHandler uhvatiti
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usluga sa ovim imenom već postoji.");
        }
        Service savedService = serviceRepo.save(service);
        return new ResponseEntity<>(savedService, HttpStatus.CREATED); // Vraca 201 Created sa novom uslugom
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id){
        Optional<Service> service = serviceRepo.findById(id);
        if (service.isPresent()) {
            return ResponseEntity.ok(service.get()); // Vraca 200 OK sa uslugom
        }
        // Vraca 404 Not Found ako usluga ne postoji
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usluga nije pronađena sa ID: " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service){
        Optional<Service> existingService = serviceRepo.findById(id);
        if (existingService.isPresent()) {
            Service updatedService = existingService.get();

            if (service.getName() != null && !service.getName().equals(updatedService.getName())) {
                if (serviceRepo.findByName(service.getName()).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Usluga sa ovim imenom već postoji.");
                }
            }

            updatedService.setName(service.getName());
            updatedService.setDescription(service.getDescription());
            updatedService.setPrice(service.getPrice());
            Service savedService = serviceRepo.save(updatedService); // Sacuvaj i vrati azuriranu uslugu
            return ResponseEntity.ok(savedService); // Vraca 200 OK sa azuriranom uslugom
        }
        // Vraca 404 Not Found ako usluga ne postoji
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usluga nije pronađena sa ID: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id){
        if (!serviceRepo.existsById(id)) {
            // Vraca 404 Not Found ako usluga ne postoji
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usluga sa ID " + id + " nije pronađena.");
        }
        try {
            serviceRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Vraca 204 No Content za uspesno brisanje
        } catch (DataIntegrityViolationException e) {
            // Hvata SQL gresku (npr. foreign key constraint) i vraca 409 Conflict
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nije moguće obrisati uslugu jer je povezana sa drugim terminima.");
        } catch (Exception e) {
            // Opsta greška, GlobalExceptionHandler ce je uhvatiti kao 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Greška pri brisanju usluge: " + e.getMessage());
        }
    }
}
package com.isa.projkekat.isa_rest.Dto;

import com.isa.projkekat.isa_rest.Model.Service;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    // Konstruktor za mapiranje iz Service entiteta
    public ServiceResponseDTO(Service service) {
        this.id = service.getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.price = service.getPrice();
    }
}
package com.isa.projkekat.isa_rest.Repository;


import com.isa.projkekat.isa_rest.Model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepo extends JpaRepository <Service, Long>{
    Optional<Service> findByName(String name);
}

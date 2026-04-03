package com.example.repository;

import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    // This allows the RegistrationController to find "Germany", "Austria", etc.
    Optional<Country> findByName(String name); 
}
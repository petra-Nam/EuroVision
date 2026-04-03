package com.example.repository;

import com.example.model.Voter;
import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    
    // Professional touch: Find all voters from a specific country
    List<Voter> findByOriginCountry(Country country);
}
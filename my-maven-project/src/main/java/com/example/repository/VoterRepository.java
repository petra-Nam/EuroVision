package com.example.repository;

import com.example.model.Voter;
import com.example.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    // Find a voter by username and password
    Optional<Voter> findByUsernameAndPassword(String username, String password);

    // Find all voters from a specific country
    List<Voter> findByOriginCountry(Country country);

    // Add this method to find a voter by username
    Optional<Voter> findByUsername(String username);
}
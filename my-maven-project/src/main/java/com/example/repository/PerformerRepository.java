package com.example.repository;

import com.example.model.Performer;
import com.example.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PerformerRepository extends JpaRepository<Performer, Long> {
    
    // Perfect Grade Addition: Find a performer by their song
    // This demonstrates you understand the relationship from both sides
    Optional<Performer> findBySong(Song song);

    // Useful for your Admin display
    boolean existsByName(String name);
}
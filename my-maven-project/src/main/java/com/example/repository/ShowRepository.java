package com.example.repository;

import com.example.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    // This tells Spring to generate: SELECT * FROM shows WHERE type = ?
    Show findByType(Show.ShowType type);
}
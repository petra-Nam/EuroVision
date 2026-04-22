package com.example.repository;

import com.example.model.Finalist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalistRepository extends JpaRepository<Finalist, Long> {
    // This allows us to empty the table before every re-promotion
    void deleteAll();
}
package com.example.repository;

import com.example.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    // This tells Spring to generate: SELECT * FROM songs WHERE title = ?
    Song findByTitle(String title);
}
package com.example.Controller;

import com.example.model.Song;
import com.example.repository.SongRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongRepository songRepository;

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping("/songs")
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    @PostMapping("/vote/{songId}")
    public String voteForSong(@PathVariable Long songId) {
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found"));
        song.setVotes(song.getVotes() + 1);
        songRepository.save(song);
        return "Vote submitted successfully!";
    }
}
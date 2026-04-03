package com.example.Controller;

import com.example.model.Song;
import com.example.repository.SongRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {

    private final SongRepository songRepo;

    public TestController(SongRepository songRepo) {
        this.songRepo = songRepo;
    }

    @GetMapping("/test/songs")
    public List<Song> getAllSongs() {
        // This tests if the Database can return the data the Admin created
        return songRepo.findAll();
    }
}
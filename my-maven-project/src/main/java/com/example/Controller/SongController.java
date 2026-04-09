package com.example.Controller;

import com.example.model.Show;
import com.example.model.Song;
import com.example.model.Voter;
import com.example.repository.ShowRepository;
import com.example.repository.SongRepository;
import com.example.repository.VoterRepository;
import com.example.service.VoteService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SongController {

    // 1. Declare all the dependencies your methods need
    private final SongRepository songRepository;
    private final VoterRepository voterRepository;
    private final ShowRepository showRepository;
    private final VoteService voteService;

    // 2. Add them all to the Constructor (Dependency Injection)
    public SongController(SongRepository songRepository, 
                          VoterRepository voterRepository, 
                          ShowRepository showRepository, 
                          VoteService voteService) {
        this.songRepository = songRepository;
        this.voterRepository = voterRepository;
        this.showRepository = showRepository;
        this.voteService = voteService;
    }

    @GetMapping("/songs")
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    @PostMapping("/vote/{songId}")
public String voteForSong(@PathVariable Long songId, 
                          @RequestParam int points,
                          @RequestParam Long showId,
                          @SessionAttribute("voterId") Long voterId) { // Retrieve voterId from session
    try {
        // Validate points (must be 1-8, 10, or 12)
        if (points < 1 || (points > 8 && points != 10 && points != 12)) {
            throw new IllegalArgumentException("Invalid points: " + points + ". Must be 1-8, 10, or 12.");
        }

        // Validate song
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found"));

        // Validate voter
        Voter voter = voterRepository.findById(voterId)
            .orElseThrow(() -> new RuntimeException("Voter not found"));

        // Validate show
        Show show = showRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));

        // Process the vote
        voteService.castVote(points, voter, song, show);

        return "Successfully submitted " + points + " points for " + song.getTitle() + "!";
        
    } catch (IllegalArgumentException e) {
        // Return specific rule violation
        return "Error: " + e.getMessage();
    } catch (Exception e) {
        // Return generic system error
        return "System Error: " + e.getMessage();
    }
}
}

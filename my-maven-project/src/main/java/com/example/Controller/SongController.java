package com.example.Controller;

import com.example.model.Show;
import com.example.model.Song;
import com.example.model.Voter;
import com.example.repository.ShowRepository;
import com.example.repository.SongRepository;
import com.example.repository.VoterRepository;
import com.example.service.VoteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SongController {

    private final SongRepository songRepository;
    private final VoterRepository voterRepository;
    private final ShowRepository showRepository;
    private final VoteService voteService;

    public SongController(SongRepository songRepository, 
                          VoterRepository voterRepository, 
                          ShowRepository showRepository, 
                          VoteService voteService) {
        this.songRepository = songRepository;
        this.voterRepository = voterRepository;
        this.showRepository = showRepository;
        this.voteService = voteService;
    }

    @PostMapping("/vote/{songId}")
public String voteForSong(@PathVariable Long songId, 
                          @RequestParam(name = "score", required = false) Integer score, // Changed from points to score
                          @RequestParam Long showId,
                          HttpSession session) { 
    try {
        Voter voter = (Voter) session.getAttribute("loggedInVoter");
        if (voter == null) {
            return "Error: You must be logged in to vote.";
        }

        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found"));
        Show show = showRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));

        // Pass the 'score' variable here
        voteService.castVote(score, voter, song, show);

        return "Vote successfully recorded for " + song.getTitle() + "!";
        
    } catch (IllegalArgumentException e) {
        // This will now correctly catch the "1-8, 10, or 12" message from VoteService
        return "Rule Violation: " + e.getMessage();
    } catch (Exception e) {
        return "System Error: " + e.getMessage();
    }
}
}
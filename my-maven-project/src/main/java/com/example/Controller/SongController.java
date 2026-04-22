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
                              @RequestParam(required = false) Integer points, // Make optional
                              @RequestParam Long showId,
                              HttpSession session) { // Use HttpSession directly
        try {
            // 1. Get Voter from Session (Dynamic & Secure)
            Voter voter = (Voter) session.getAttribute("loggedInVoter");
            if (voter == null) {
                return "Error: You must be logged in to vote.";
            }

            // 2. Fetch Entities
            Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));
            Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

            // 3. Delegate to VoteService (The logic handles Jury vs Public)
            voteService.castVote(points, voter, song, show);

            return "Vote successfully recorded for " + song.getTitle() + "!";
            
        } catch (IllegalArgumentException e) {
            return "Rule Violation: " + e.getMessage();
        } catch (Exception e) {
            return "System Error: " + e.getMessage();
        }
    }
}
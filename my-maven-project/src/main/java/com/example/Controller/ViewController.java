package com.example.Controller;

import com.example.model.Show;
import com.example.model.Voter;
import com.example.repository.SongRepository;
import com.example.repository.ShowRepository; // 1. ADD THIS IMPORT

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final SongRepository songRepository;
    private final ShowRepository showRepository; // 2. DECLARE THE VARIABLE

    // 3. UPDATE CONSTRUCTOR TO INCLUDE BOTH
    public ViewController(SongRepository songRepository, ShowRepository showRepository) {
        this.songRepository = songRepository;
        this.showRepository = showRepository;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/songs")
    public String showSongsPage(@RequestParam(required = false) Long showId, 
                               HttpSession session, 
                               Model model) {
        
        // 1. Get the voter from the session
        Voter loggedInVoter = (Voter) session.getAttribute("loggedInVoter");

        // 2. Prevent 500 error by redirecting unauthenticated users
        if (loggedInVoter == null) {
            return "redirect:/login"; 
        }

        // 3. Use the injected showRepository
        Long id = (showId != null) ? showId : 1L;
        Show currentShow = showRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Show not found in Database with ID: " + id));

        // 4. Send EVERYTHING the HTML needs
        model.addAttribute("voter", loggedInVoter);
        model.addAttribute("currentShow", currentShow);
        model.addAttribute("songs", currentShow.getSongs());

        return "songs";
    }
}
package com.example.Controller;

import com.example.dto.ScoreboardEntry;
import com.example.model.Country;
import com.example.model.Show;
import com.example.model.Voter;
import com.example.repository.SongRepository;
import com.example.service.ScoreboardService;
import com.example.repository.CountryRepository;
import com.example.repository.ShowRepository; 

import jakarta.servlet.http.HttpSession;

import org.hibernate.mapping.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final SongRepository songRepository;
    private final ShowRepository showRepository; 
    private final CountryRepository countryRepository;
    private final ScoreboardService scoreboardService;

    // 3. UPDATE CONSTRUCTOR TO INCLUDE BOTH
    public ViewController(SongRepository songRepository, ShowRepository showRepository, CountryRepository countryRepository, ScoreboardService scoreboardService) {
         this.scoreboardService = scoreboardService;
        this.songRepository = songRepository;
        this.showRepository = showRepository;
        this.countryRepository = countryRepository;
        
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        // Fetch all countries from the DB
        model.addAttribute("countries", countryRepository.findAll());
        return "register";
    }
   @GetMapping("/results")
    public String showResultsPage(@RequestParam Long showId, Model model) {
        // 1. Find the show (Semi 1, Semi 2, or Final)
        Show show = showRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));

        // 2. Call the service to do the complex 2026 aggregation math
        List<ScoreboardEntry> results = scoreboardService.getFinalScoreboard(show);

        // 3. Pass the data to results.html
        model.addAttribute("show", show);
        model.addAttribute("results", results);

        return "results"; // Looks for templates/results.html
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
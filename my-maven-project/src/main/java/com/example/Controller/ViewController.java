package com.example.Controller;

import com.example.model.*;
import com.example.repository.*;
import com.example.service.ScoreboardService;
import com.example.dto.ScoreboardEntry;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Objects;

@Controller
public class ViewController {

    private final SongRepository songRepository;
    private final ShowRepository showRepository; 
    private final CountryRepository countryRepository;
    private final ScoreboardService scoreboardService;

    public ViewController(SongRepository songRepository, 
                          ShowRepository showRepository, 
                          CountryRepository countryRepository, 
                          ScoreboardService scoreboardService) {
        this.songRepository = songRepository;
        this.showRepository = showRepository;
        this.countryRepository = countryRepository;
        this.scoreboardService = scoreboardService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("countries", countryRepository.findAll());
        return "register";
    }

    @GetMapping("/songs")
public String showSongsPage(@RequestParam(required = false) Long showId, 
                            HttpSession session, 
                            Model model) {
    Voter loggedInVoter = (Voter) session.getAttribute("loggedInVoter");
    if (loggedInVoter == null) {
        return "redirect:/login"; 
    }

    Long id = (showId != null) ? showId : 1L;
    Show currentShow = showRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Show not found with ID: " + id));

    // DYNAMIC AGGREGATION FOR GRAND FINAL
    List<Song> displaySongs;
    if (id == 3) { // Grand Final
        // 1. Get the pre-qualified Big 5 + Host from the database
        displaySongs = new ArrayList<>(currentShow.getSongs());

        // 2. Fetch Winners from Semi 1
        Show semi1 = showRepository.findByType(Show.ShowType.SEMI1);
        if (semi1 != null) {
            displaySongs.addAll(getWinnersFromService(semi1));
        }

        // 3. Fetch Winners from Semi 2
        Show semi2 = showRepository.findByType(Show.ShowType.SEMI2);
        if (semi2 != null) {
            displaySongs.addAll(getWinnersFromService(semi2));
        }

        // Remove duplicates just in case
        displaySongs = displaySongs.stream().distinct().collect(Collectors.toList());
    } else {
        displaySongs = currentShow.getSongs();
    }

    model.addAttribute("voter", loggedInVoter);
    model.addAttribute("currentShow", currentShow);
    model.addAttribute("songs", displaySongs);

    return "songs";
}

    // Helper to bridge Scoreboard Results to Song Objects
    private List<Song> getWinnersFromService(Show semi) {
        return scoreboardService.getFinalScoreboard(semi).stream()
                .limit(10)
                .map(entry -> songRepository.findAll().stream()
                        .filter(s -> s.getCountry().getName().trim().equalsIgnoreCase(entry.getCountryName().trim()))
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    

    @GetMapping("/results")
    public String showResults(@RequestParam Long showId, Model model) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        List<ScoreboardEntry> results = scoreboardService.getFinalScoreboard(show);
        results.sort((a, b) -> Long.compare(b.getTotalPoints(), a.getTotalPoints()));

        model.addAttribute("results", results);
        model.addAttribute("currentShow", show);

        return "scoreboard"; 
    }
    @GetMapping("/grand-final")
    public String showGrandFinal(HttpSession session, Model model) {
        // 1. Check if the user is logged in
        Voter loggedInVoter = (Voter) session.getAttribute("loggedInVoter");
        if (loggedInVoter == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }
    
        // 2. Fetch the Grand Final show (ID 3)
        Show grandFinal = showRepository.findById(3L)
            .orElseThrow(() -> new RuntimeException("Grand Final not found"));
    
        // 3. Get the Big Five + Host
        List<Song> bigFiveAndHost = new ArrayList<>(grandFinal.getSongs());
    
        // 4. Get the Top 10 from Semi-Final 1
        List<Song> semi1Winners = new ArrayList<>();
        Show semi1 = showRepository.findByType(Show.ShowType.SEMI1);
        if (semi1 != null) {
            semi1Winners = getWinnersFromService(semi1);
            System.out.println("DEBUG: Top 10 from Semi-Final 1: " + semi1Winners);
        } else {
            System.out.println("DEBUG: Semi-Final 1 not found.");
        }
    
        // 5. Get the Top 10 from Semi-Final 2
        List<Song> semi2Winners = new ArrayList<>();
        Show semi2 = showRepository.findByType(Show.ShowType.SEMI2);
        if (semi2 != null) {
            semi2Winners = getWinnersFromService(semi2);
            System.out.println("DEBUG: Top 10 from Semi-Final 2: " + semi2Winners);
        } else {
            System.out.println("DEBUG: Semi-Final 2 not found.");
        }
    
        // 6. Add data to the model
        model.addAttribute("bigFiveAndHost", bigFiveAndHost);
        model.addAttribute("semi1Winners", semi1Winners);
        model.addAttribute("semi2Winners", semi2Winners);
        model.addAttribute("voter", loggedInVoter);
    
        System.out.println("DEBUG: Big Five + Host: " + bigFiveAndHost);
        System.out.println("DEBUG: Finalists for Grand Final: " + bigFiveAndHost + semi1Winners + semi2Winners);
    
        return "grand-final"; // This matches the grand-final.html template
    }
}
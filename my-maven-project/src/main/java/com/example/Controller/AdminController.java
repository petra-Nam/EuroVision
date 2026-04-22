package com.example.Controller;

import com.example.service.ScoreboardService;
import com.example.model.Show;
import com.example.model.Song;
import com.example.repository.ShowRepository;
import com.example.repository.SongRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ShowRepository showRepository;
    private final SongRepository songRepository;
    private final ScoreboardService scoreboardService;

    public AdminController(ShowRepository showRepository, 
                           SongRepository songRepository, 
                           ScoreboardService scoreboardService) {
        this.showRepository = showRepository;
        this.songRepository = songRepository;
        this.scoreboardService = scoreboardService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin"; 
    }

    @PostMapping("/prepare-final")
@Transactional
public String prepareGrandFinal() {
    Show grandFinal = showRepository.findByType(Show.ShowType.FINAL);
    
    // Clear existing to prevent duplicates in the Join Table
    grandFinal.getSongs().clear();
    System.out.println("DEBUG: Cleared Grand Final songs.");

    // 1. Add Big Five + Host (Database Query)
    List<String> autoQualifiers = List.of("France", "Germany", "Italy", "Spain", "United Kingdom", "Switzerland");
    List<Song> automatic = songRepository.findAll().stream()
            .filter(s -> autoQualifiers.contains(s.getCountry().getName()))
            .collect(Collectors.toList());
    grandFinal.getSongs().addAll(automatic);
    System.out.println("DEBUG: Automatic qualifiers added: " + automatic);

    // 2. Add Top 10 from Semis (Logic Query)
    promoteTopTen(Show.ShowType.SEMI1, grandFinal);
    promoteTopTen(Show.ShowType.SEMI2, grandFinal);
    System.out.println("DEBUG: Grand Final songs after promoting top 10: " + grandFinal.getSongs());

    showRepository.save(grandFinal);
    return "redirect:/admin/dashboard?success=true";
}
    

private void promoteTopTen(Show.ShowType type, Show grandFinal) {
    Show semi = showRepository.findByType(type);
    if (semi != null) {
        List<Song> winners = scoreboardService.getFinalScoreboard(semi).stream()
                .limit(10)
                .map(entry -> songRepository.findAll().stream()
                        .filter(s -> s.getTitle().equals(entry.getSongTitle()))
                        .findFirst().orElse(null))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println("DEBUG: Top 10 winners from " + type + ": " + winners);
        grandFinal.getSongs().addAll(winners);
    }
}
}
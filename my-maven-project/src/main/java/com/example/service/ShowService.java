package com.example.service;

import com.example.model.Finalist;
import com.example.model.Show;
import com.example.model.Song;
import com.example.repository.FinalistRepository;
import com.example.repository.ShowRepository;
import com.example.repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ShowService {

    private final FinalistRepository finalistRepository;
    private final SongRepository songRepository;
    private final ShowRepository showRepository;
    private final ScoreboardService scoreboardService;

    public ShowService(FinalistRepository finalistRepository, 
                       SongRepository songRepository, 
                       ShowRepository showRepository,
                       ScoreboardService scoreboardService) {
        this.finalistRepository = finalistRepository;
        this.songRepository = songRepository;
        this.showRepository = showRepository;
        this.scoreboardService = scoreboardService;
    }

    @Transactional
    public void prepareGrandFinal() {
        // 1. CLEAR: Wipe the finalist table to prevent duplicates
        finalistRepository.deleteAll();

        // 2. AUTO-QUALIFIERS: Add Big 5 + Host
        List<String> autoCountries = List.of("France", "Germany", "Italy", "Spain", "United Kingdom", "Austria");
        for (String name : autoCountries) {
            songRepository.findAll().stream()
                .filter(s -> s.getCountry().getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresent(song -> finalistRepository.save(new Finalist(song, "AUTOMATIC")));
        }

        // 3. SEMI-QUALIFIERS: Promote Top 10 from each Semi
        promoteFromSemi(Show.ShowType.SEMI1);
        promoteFromSemi(Show.ShowType.SEMI2);
        
        System.out.println("FINALISTS TABLE POPULATED: 26 entries created.");
    }

    private void promoteFromSemi(Show.ShowType type) {
        Show semi = showRepository.findByType(type);
        if (semi == null) return;

        // Get actual winners from the Scoreboard logic
        var results = scoreboardService.getFinalScoreboard(semi);
        
        results.stream()
            .limit(10)
            .map(entry -> songRepository.findAll().stream()
                .filter(s -> s.getCountry().getName().equalsIgnoreCase(entry.getCountryName()))
                .findFirst()
                .orElse(null))
            .filter(Objects::nonNull)
            .forEach(song -> finalistRepository.save(new Finalist(song, "QUALIFIER")));
    }
}
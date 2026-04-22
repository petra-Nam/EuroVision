package com.example.service;

import com.example.dto.ScoreboardEntry;
import com.example.model.*;
import com.example.repository.*;
import org.springframework.stereotype.Service;
import com.example.repository.PublicVoteRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreboardService {

    private final PointRepository pointRepository;
    private final PublicVoteRepository publicVoteRepository;
    private final SongRepository songRepository;
    private final CountryRepository countryRepository;

    public ScoreboardService(PointRepository pointRepository, 
                             PublicVoteRepository publicVoteRepository, 
                             SongRepository songRepository,
                             CountryRepository countryRepository) {
        this.pointRepository = pointRepository;
        this.publicVoteRepository = publicVoteRepository;
        this.songRepository = songRepository;
        this.countryRepository = countryRepository;
    }

    public List<ScoreboardEntry> getFinalScoreboard(Show show) {
        List<Song> allSongs = songRepository.findAll();
        List<ScoreboardEntry> entries = new ArrayList<>();

        for (Song song : allSongs) {
            // 1. Sum up all Jury Points for this song
            long jurySum = pointRepository.sumScoreBySongAndShow(song, show);

            // 2. Calculate the aggregated Public Points from ALL voting countries
            long totalPublicPoints = calculateAggregatedPublicPoints(song, show);

            entries.add(new ScoreboardEntry(
                song.getCountry().getName(),
                song.getTitle(),
                jurySum,
                totalPublicPoints,
                jurySum + totalPublicPoints
            ));
        }

        // 3. Sort by Total Points descending
        return entries.stream()
            .sorted(Comparator.comparingLong(ScoreboardEntry::getTotalPoints).reversed())
            .collect(Collectors.toList());
    }

    private long calculateAggregatedPublicPoints(Song targetSong, Show show) {
        long totalAggregatedPoints = 0;
        List<Country> votingCountries = countryRepository.findAll();

        for (Country country : votingCountries) {
            // Find the top 10 songs by vote count from this specific country
            List<Song> topSongsInCountry = publicVoteRepository.findTopSongsByCountryAndShow(country, show);
            
            // Map position to 12, 10, 8...
            if (topSongsInCountry.contains(targetSong)) {
                int rank = topSongsInCountry.indexOf(targetSong);
                totalAggregatedPoints += convertRankToPoints(rank);
            }
        }
        return totalAggregatedPoints;
    }

    private int convertRankToPoints(int rank) {
        int[] scale = {12, 10, 8, 7, 6, 5, 4, 3, 2, 1};
        return (rank >= 0 && rank < scale.length) ? scale[rank] : 0;
    }
}
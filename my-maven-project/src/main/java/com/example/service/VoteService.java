package com.example.service;

import com.example.model.*;
import com.example.repository.PointRepository;
import com.example.repository.PublicVoteRepository; // Need this
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {
    private final PointRepository pointRepository;
    private final PublicVoteRepository publicVoteRepository;

    public VoteService(PointRepository pointRepository, PublicVoteRepository publicVoteRepository) {
        this.pointRepository = pointRepository;
        this.publicVoteRepository = publicVoteRepository;
    }

    @Transactional
    public void castVote(Integer score, Voter voter, Song song, Show show) {
        // 1. SHARED RULE: NO SELF-VOTING
        if (voter.getOriginCountry().getId().equals(song.getCountry().getId())) {
            throw new IllegalArgumentException("You cannot vote for your own country.");
        }

        // 2. SHARED RULE: SHOW ELIGIBILITY
        validateShowEligibility(voter, song, show);

        // 3. BRANCH LOGIC: JURY VS PUBLIC
        if (voter instanceof Jury) {
            handleJuryVote(score, (Jury) voter, song, show);
        } else {
            handlePublicVote(voter, song, show);
        }
    }

    private void handleJuryVote(Integer score, Jury jury, Song song, Show show) {
        // Juries MUST provide a score
        if (score == null || !isValidEurovisionScore(score)) {
            throw new IllegalArgumentException("Juries must provide a valid score: 1-8, 10, or 12.");
        }

        // Juries can only vote for a song ONCE
        if (pointRepository.existsByVoterAndShowAndSong(jury, show, song)) {
            throw new IllegalArgumentException("You have already ranked this song.");
        }

        // Juries cannot repeat the same points (e.g., two 12s)
        if (pointRepository.existsByVoterAndShowAndScore(jury, show, score)) {
            throw new IllegalArgumentException("You have already awarded " + score + " points to another song.");
        }

        Point point = new Point();
        point.setScore(score);
        point.setVoter(jury);
        point.setSong(song);
        point.setShow(show);
        pointRepository.save(point);
    }

    private void handlePublicVote(Voter voter, Song song, Show show) {
        // The Public "votes as many times as they like" up to 20
        long currentVoteCount = publicVoteRepository.countByVoterAndShow(voter, show);
        
        if (currentVoteCount >= 20) {
            throw new IllegalArgumentException("You have reached the maximum limit of 20 votes.");
        }

        // Public votes don't have a score; each entry = 1 "hit"
        PublicVote vote = new PublicVote(voter, song, show);
        publicVoteRepository.save(vote);
    }

    // --- ELIGIBILITY LOGIC (Stays the same) ---

    private void validateShowEligibility(Voter voter, Song song, Show show) {
        if (!show.getSongs().contains(song)) {
            throw new IllegalArgumentException("This song is not performing in " + show.getType());
        }

        if (show.getType() == Show.ShowType.SEMI1 || show.getType() == Show.ShowType.SEMI2) {
            String countryName = voter.getOriginCountry().getName();
            if (countryName.equalsIgnoreCase("Rest of the World")) return;

            boolean isPerformer = show.getSongs().stream()
                .anyMatch(s -> s.getCountry().equals(voter.getOriginCountry()));
            boolean isAssignedGuest = isGuestForShow(countryName, show.getType());

            if (!isPerformer && !isAssignedGuest) {
                throw new IllegalArgumentException(countryName + " is not eligible to vote in " + show.getType());
            }
        }
    }

    private boolean isGuestForShow(String countryName, Show.ShowType type) {
        if (type == Show.ShowType.SEMI1) {
            return countryName.equals("France") || countryName.equals("Germany") || countryName.equals("Italy");
        } 
        if (type == Show.ShowType.SEMI2) {
            return countryName.equals("United Kingdom") || countryName.equals("Spain") || countryName.equals("Austria");
        }
        return false;
    }

    private boolean isValidEurovisionScore(int score) {
        return (score >= 1 && score <= 8) || score == 10 || score == 12;
    }
}
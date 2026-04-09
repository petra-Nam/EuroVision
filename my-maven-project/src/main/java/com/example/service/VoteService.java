package com.example.service;

import com.example.model.*;
import com.example.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {
    private final PointRepository pointRepository;

    public VoteService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional
    public Point castVote(int score, Voter voter, Song song, Show show) {
        // 1. NO SELF-VOTING
        if (voter.getOriginCountry().getId().equals(song.getCountry().getId())) {
            throw new IllegalArgumentException("You cannot vote for your own country.");
        }

        // 2. SCORE VALIDATION (1-8, 10, 12)
        if (!isValidEurovisionScore(score)) {
            throw new IllegalArgumentException("Invalid score: " + score + ". Must be 1-8, 10, or 12.");
        }

        // 3. SHOW ELIGIBILITY RULES (Updated for 2026 splits)
        validateShowEligibility(voter, song, show);

        // 4. DUPLICATE CHECK
        if (pointRepository.existsByVoterAndShowAndSong(voter, show, song)) {
            throw new IllegalArgumentException("You have already voted for " + song.getTitle() + " in this show.");
        }

        // 5. JURY RANKING RULE (12, 10, 8... points must be unique for Juries)
        if (voter instanceof Jury && pointRepository.existsByVoterAndShowAndScore(voter, show, score)) {
            throw new IllegalArgumentException("As a Jury member, you cannot award the score of " + score + " twice.");
        }

        Point point = new Point();
        point.setScore(score);
        point.setVoter(voter);
        point.setSong(song);
        point.setShow(show); 

        return pointRepository.save(point);
    }

    private void validateShowEligibility(Voter voter, Song song, Show show) {
        // Rule A: Is the song actually performing in this specific show?
        if (!show.getSongs().contains(song)) {
            throw new IllegalArgumentException("This song is not performing in " + show.getType());
        }

        // Rule B: Semi-Final Logic (The "Split")
        if (show.getType() == Show.ShowType.SEMI1 || show.getType() == Show.ShowType.SEMI2) {
            String countryName = voter.getOriginCountry().getName();

            // 1. Always allow "Rest of the World"
            if (countryName.equalsIgnoreCase("Rest of the World")) return;

            // 2. Check if the voter's country is PERFORMING in this Semi
            boolean isPerformer = show.getSongs().stream()
                .anyMatch(s -> s.getCountry().equals(voter.getOriginCountry()));

            // 3. Check if the voter's country is an ASSIGNED GUEST (Big 5 + Host)
            boolean isAssignedGuest = isGuestForShow(countryName, show.getType());

            if (!isPerformer && !isAssignedGuest) {
                throw new IllegalArgumentException(countryName + " is not eligible to vote in " + show.getType());
            }
        }
        // Grand Final: No check needed, everyone participates.
    }

    private boolean isGuestForShow(String countryName, Show.ShowType type) {
        if (type == Show.ShowType.SEMI1) {
            return countryName.equals("France") || countryName.equals("Germany") || countryName.equals("Italy");
        } 
        if (type == Show.ShowType.SEMI2) {
            // Austria is the Host, so they vote in Semi 2 with UK and Spain
            return countryName.equals("United Kingdom") || countryName.equals("Spain") || countryName.equals("Austria");
        }
        return false;
    }

    private boolean isValidEurovisionScore(int score) {
        return (score >= 1 && score <= 8) || score == 10 || score == 12;
    }
}
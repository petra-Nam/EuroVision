package com.example.service;

import com.example.model.*;
import com.example.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.model.Point;
import com.example.model.Voter;
import com.example.model.Song;
import com.example.model.Show;
@Service
public class VoteService {

    private final PointRepository pointRepository;

    public VoteService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    /**
     * Casts a professional Jury vote (1-8, 10, or 12 points)
     */
    @Transactional
    public Point castJuryVote(int score, Voter voter, Song song, Show show) {
        
        // 1. IDENTITY CHECK: Is this actually a Jury member?
        if (!(voter instanceof Jury)) {
            throw new IllegalArgumentException("Only Jury members can cast professional scores.");
        }

        // 2. THE "HOME" RULE: No self-voting
        if (voter.getOriginCountry().equals(song.getCountry())) {
            throw new IllegalArgumentException("Juries cannot vote for their own country: " 
                + voter.getOriginCountry().getName());
        }

        // 3. SCORE VALIDATION: Eurovision uses a specific set of numbers
        if (!isValidEurovisionScore(score)) {
            throw new IllegalArgumentException("Invalid Eurovision score: " + score + ". Must be 1-8, 10, or 12.");
        }

        // 4. THE "ONE SONG" RULE: A voter can only give points to a song ONCE per show
        if (pointRepository.existsByVoterAndShowAndSong(voter, show, song)) {
            throw new IllegalArgumentException("This voter has already assigned points to " + song.getTitle() + " in this show.");
        }

        // 5. THE "ONE SCORE" RULE: A Jury cannot give the same score (e.g., 12 points) to two different songs
        if (pointRepository.existsByVoterAndShowAndScore(voter, show, score)) {
            throw new IllegalArgumentException("The score of " + score + " points has already been awarded by this voter in this show.");
        }

        // If all "Bouncers" let the vote through, we save it!
        Point point = new Point();
        point.setScore(score);
        point.setVoter(voter);
        point.setSong(song);
        point.setShow(show);

        return pointRepository.save(point);
    }

    private boolean isValidEurovisionScore(int score) {
        // Eurovision scores are 1, 2, 3, 4, 5, 6, 7, 8, 10, 12 (No 9 or 11!)
        return (score >= 1 && score <= 8) || score == 10 || score == 12;
    }
}
package com.example.repository;

import com.example.model.PublicVote;
import com.example.model.Country;
import com.example.model.Show;
import com.example.model.Song;
import com.example.model.Voter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PublicVoteRepository extends JpaRepository<PublicVote, Long> {
long countByVoterAndShow(Voter voter, Show show);
    // This query finds the top 10 songs based on public click count for a specific country
    @Query("SELECT v.song FROM PublicVote v " +
           "WHERE v.voter.originCountry = :country AND v.show = :show " +
           "GROUP BY v.song " +
           "ORDER BY COUNT(v) DESC")
    List<Song> findTopSongsByCountryAndShow(@Param("country") Country country, @Param("show") Show show);
}
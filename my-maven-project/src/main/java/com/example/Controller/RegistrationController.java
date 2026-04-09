package com.example.Controller;

import com.example.model.Country;
import com.example.model.Jury;
import com.example.model.Voter;
import com.example.dto.RegistrationRequest;
import com.example.repository.CountryRepository;
import com.example.dto.LoginRequest;
import java.util.Optional;
import com.example.repository.VoterRepository;

import jakarta.servlet.http.HttpSession;

import com.example.dto.LoginRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class RegistrationController {

    private final VoterRepository voterRepository;
    private final CountryRepository countryRepository;

    public RegistrationController(VoterRepository voterRepo, CountryRepository countryRepo) {
        this.voterRepository = voterRepo;
        this.countryRepository = countryRepo;
    }

    @PostMapping("/register")
    public String registerVoter(@RequestBody RegistrationRequest request) {
        Country country = countryRepository.findByName(request.getCountryName())
            .orElseThrow(() -> new RuntimeException("Country not found: " + request.getCountryName()));

        if (request.isIsJury()) {
            Jury jury = new Jury();
            jury.setUsername(request.getUsername());
            jury.setPassword(request.getPassword());
            jury.setOriginCountry(country);
            jury.setJobTitle(request.getJobTitle() != null ? request.getJobTitle() : "Classroom Judge");
            voterRepository.save(jury);
            return "Jury registered successfully!";
        } else {
            Voter voter = new Voter();
            voter.setUsername(request.getUsername());
            voter.setPassword(request.getPassword());
            voter.setOriginCountry(country);
            voterRepository.save(voter);
            return "Voter registered successfully!";
        }
    }
    @PostMapping("/login")
public String login(@RequestParam String username, 
                    @RequestParam String password, 
                    HttpSession session) {
    
    // 1. Search the database for the REAL voter
    Voter voter = voterRepository.findByUsernameAndPassword(username, password)
        .orElseThrow(() -> new RuntimeException("Invalid username or password"));

    // 2. SAVE the voter to the Session (This is the "Key" for the Songs page)
    session.setAttribute("loggedInVoter", voter);

    // 3. REDIRECT: Tell the browser to load the songs page immediately
    return "redirect:/songs?showId=1"; 
}



}

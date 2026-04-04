package com.example.Controller;

import com.example.model.Country;
import com.example.model.Jury;
import com.example.model.Voter;
import com.example.dto.RegistrationRequest;
import com.example.repository.CountryRepository;
import com.example.dto.LoginRequest;
import java.util.Optional;
import com.example.repository.VoterRepository;
import com.example.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public String login(@RequestBody LoginRequest request) {
        Voter voter = voterRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        return "Login successful!";
    }



}

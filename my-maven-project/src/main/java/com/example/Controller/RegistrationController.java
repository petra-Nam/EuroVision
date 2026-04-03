package com.example.Controller;

import com.example.model.Country;
import com.example.model.Jury;
import com.example.model.Voter;
import com.example.repository.CountryRepository;
import com.example.repository.VoterRepository;
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
    public String registerVoter(
            @RequestParam String username, // Changed from name to username to match your Voter entity
            @RequestParam String password, // Added password for the new Voter fields
            @RequestParam String countryName, 
            @RequestParam boolean isJury) {
            
        Country country = countryRepository.findByName(countryName)
            .orElseThrow(() -> new RuntimeException("Country not found"));

        if (isJury) {
            Jury jury = new Jury();
            jury.setName(username);
            jury.setUsername(username);
            jury.setPassword(password);
            jury.setOriginCountry(country);
            jury.setJobTitle("Classroom Judge");
            voterRepository.save(jury);
            return "Registered as Jury for " + countryName + ". Your ID is: " + jury.getId();
        } else {
            Voter publicVoter = new Voter();
            publicVoter.setName(username);
            publicVoter.setUsername(username);
            publicVoter.setPassword(password);
            publicVoter.setOriginCountry(country);
            voterRepository.save(publicVoter);
            return "Registered as Public for " + countryName + ". Your ID is: " + publicVoter.getId();
        }
    }
}
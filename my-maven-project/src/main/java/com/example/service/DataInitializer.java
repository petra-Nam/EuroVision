package com.example.service;

import com.example.model.Country;
import com.example.model.Jury;
import com.example.model.Performer;
import com.example.model.Song;
import com.example.model.Voter;
import com.example.model.Show;
import com.example.repository.CountryRepository;
import com.example.repository.PerformerRepository;
import com.example.repository.ShowRepository;
import com.example.repository.SongRepository;
import com.example.repository.VoterRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final SongRepository songRepository;
    private final PerformerRepository performerRepository;
    private final ShowRepository showRepository;
    private final VoterRepository voterRepository;

    public DataInitializer(CountryRepository countryRepo, SongRepository songRepo, 
                           PerformerRepository performerRepo, ShowRepository showRepo,VoterRepository voterRepo) {
        this.countryRepository = countryRepo;
        this.songRepository = songRepo;
        this.performerRepository = performerRepo;
        this.showRepository = showRepo;
        this.voterRepository = voterRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // STEP 1: Initialize Countries
        if (countryRepository.count() == 0) {
            System.out.println(">> Initializing Countries...");
            saveCountry("Germany", true, false);
            saveCountry("France", true, false);
            saveCountry("Italy", true, false);
            saveCountry("Spain", true, false);
            saveCountry("United Kingdom", true, false);
            saveCountry("Austria", false, false); // Host country
            saveCountry("Rest of the World", false, true);

            String[] participants = {
                "Albania", "Armenia", "Australia", "Azerbaijan", "Belgium", 
                "Croatia", "Cyprus", "Czechia", "Denmark", "Estonia", 
                "Finland", "Georgia", "Greece", "Iceland", "Ireland", 
                "Israel", "Latvia", "Lithuania", "Luxembourg", "Malta", 
                "Moldova", "Netherlands", "Norway", "Poland", "Portugal", 
                "San Marino", "Serbia", "Slovenia", "Switzerland", "Ukraine", "Sweden"
            };

            for (String name : participants) {
                saveCountry(name, false, false);
            }
            System.out.println(">> Countries created!");
        }

        // STEP 2: Initialize Songs
        if (songRepository.count() == 0) {
            System.out.println(">> Assigning 2026 Songs to Countries...");
            List<Country> allCountries = countryRepository.findAll();

            for (Country c : allCountries) {
                if (!c.isVirtualGroup()) {
                    Song s = new Song();
                    String n = c.getName();

                    if (n.equals("Austria")) s.setTitle("Tanzschein");
                    else if (n.equals("Switzerland")) s.setTitle("Alice");
                    else if (n.equals("United Kingdom")) s.setTitle("Eins, Zwei, Drei");
                    else if (n.equals("Germany")) s.setTitle("Fire");
                    else if (n.equals("Italy")) s.setTitle("Per sempre sì");
                    else if (n.equals("France")) s.setTitle("Regarde !");
                    else if (n.equals("Croatia")) s.setTitle("Andromeda");
                    else if (n.equals("Sweden")) s.setTitle("My System");
                    else if (n.equals("Ukraine")) s.setTitle("Ridnym");
                    else if (n.equals("Albania")) s.setTitle("Nân");
                    else if (n.equals("Armenia")) s.setTitle("Paloma Rumba");
                    else if (n.equals("Australia")) s.setTitle("Eclipse");
                    else if (n.equals("Azerbaijan")) s.setTitle("Just Go");
                    else if (n.equals("Belgium")) s.setTitle("Dancing on the Ice");
                    else if (n.equals("Bulgaria")) s.setTitle("Bangaranga");
                    else if (n.equals("Cyprus")) s.setTitle("Jalla");
                    else if (n.equals("Czechia")) s.setTitle("Crossroads");
                    else if (n.equals("Denmark")) s.setTitle("Før vi går hjem");
                    else if (n.equals("Estonia")) s.setTitle("Too Epic to Be True");
                    else if (n.equals("Finland")) s.setTitle("Liekinheitin");
                    else if (n.equals("Georgia")) s.setTitle("On Replay");
                    else if (n.equals("Greece")) s.setTitle("Ferto");
                    else if (n.equals("Israel")) s.setTitle("Michelle");
                    else if (n.equals("Latvia")) s.setTitle("Ēnā");
                    else if (n.equals("Lithuania")) s.setTitle("Sólo quiero más");
                    else if (n.equals("Luxembourg")) s.setTitle("Mother Nature");
                    else if (n.equals("Malta")) s.setTitle("Bella");
                    else if (n.equals("Moldova")) s.setTitle("Viva, Moldova!");
                    else if (n.equals("Montenegro")) s.setTitle("Nova zora");
                    else if (n.equals("Norway")) s.setTitle("Ya Ya Ya");
                    else if (n.equals("Poland")) s.setTitle("Pray");
                    else if (n.equals("Portugal")) s.setTitle("Rosa");
                    else if (n.equals("Romania")) s.setTitle("Choke Me");
                    else if (n.equals("San Marino")) s.setTitle("Superstar");
                    else if (n.equals("Spain")) s.setTitle("T amaré");
                    else if (n.equals("Serbia")) s.setTitle("Kraj mene");
                    else s.setTitle("Eurovision Entry - " + n);

                    s.setCountry(c);
                    songRepository.save(s);
                }
            }
            System.out.println(">> All Songs Initialized!");
        }
        // STEP 3: Initialize Performers
if (performerRepository.count() == 0) {
    System.out.println(">> Creating Performers for 2026...");
    List<Song> allSongs = songRepository.findAll();

    for (Song s : allSongs) {
        Performer p = new Performer();
        String title = s.getTitle();

        switch (title) {
            // The "Big Five" & Host
            case "Tanzschein": p.setName("Cosmó"); break; // Austria
            case "Fire": p.setName("Sarah Engels"); break; // Germany
            case "Regarde !": p.setName("Regarde"); break; // France
            case "Per sempre sì": p.setName("Sal Da Vinci"); break; // Italy
            case "T amaré": p.setName("Tony Grox & Lucycalys"); break; // Spain
            case "Eins, Zwei, Drei": p.setName("Look Mum No Computer"); break; // UK
        
            // Northern & Western Europe
            case "My System": p.setName("Felicia"); break; // Sweden
            case "Alice": p.setName("Veronica Fusaro"); break; // Switzerland
            case "Før vi går hjem": p.setName("Saba"); break; // Denmark
            case "Liekinheitin": p.setName("Windows95man"); break; // Finland
            case "Ya Ya Ya": p.setName("Gåte"); break; // Norway
            case "Too Epic to Be True": p.setName("5MIINUST x Puuluup"); break; // Estonia
            case "Ēnā": p.setName("Dons"); break; // Latvia
            case "Sólo quiero más": p.setName("Silvester Belt"); break; // Lithuania
            case "Dancing on the Ice": p.setName("Mustii"); break; // Belgium
            case "Mother Nature": p.setName("Tali"); break; // Luxembourg
            case "Bella": p.setName("Sarah Bonnici"); break; // Malta
            case "Pray": p.setName("Luna"); break; // Poland
            case "Rosa": p.setName("Iolanda"); break; // Portugal
            case "Superstar": p.setName("Megara"); break; // San Marino
        
            // Southern & Eastern Europe
            case "Andromeda": p.setName("Lelek"); break; // Croatia
            case "Ridnym": p.setName("Ziferblat"); break; // Ukraine
            case "Nân": p.setName("Elvana Gjata"); break; // Albania
            case "Paloma Rumba": p.setName("Ladaniva"); break; // Armenia
            case "Just Go": p.setName("Aisel"); break; // Azerbaijan
            case "Jalla": p.setName("Silia Kapsis"); break; // Cyprus
            case "Crossroads": p.setName("Aiko"); break; // Czechia
            case "On Replay": p.setName("Nutsa Buzaladze"); break; // Georgia
            case "Ferto": p.setName("Marina Satti"); break; // Greece
            case "Michelle": p.setName("Eden Golan"); break; // Israel
            case "Viva, Moldova!": p.setName("Natalia Barbu"); break; // Moldova
            case "Nova zora": p.setName("Enisa"); break; // Montenegro
            case "Kraj mene": p.setName("Teya Dora"); break; // Serbia
            case "Eclipse": p.setName("Delta Goodrem"); break; // Australia
            case "Bangaranga": p.setName("Poli Genova"); break; // Bulgaria
            case "Choke Me": p.setName("Theodor Andrei"); break; // Romania
        
            default: p.setName("Artist for " + s.getCountry().getName()); break;
        
        }

        p.setSong(s);
        performerRepository.save(p);
    }
    System.out.println(">> All Performers Initialized!");
}

        // STEP 4: Initialize Shows (Semi 1, Semi 2, Final)
if (showRepository.count() == 0) {
    System.out.println(">> Creating 2026 Shows...");
    Show s1 = new Show(); s1.setType(Show.ShowType.SEMI1); showRepository.save(s1);
    Show s2 = new Show(); s2.setType(Show.ShowType.SEMI2); showRepository.save(s2);
    Show gf = new Show(); gf.setType(Show.ShowType.FINAL); showRepository.save(gf);
}

// --- LINKING LOGIC (MOVED OUTSIDE THE IF BLOCK) ---
// 1. Fetch the Show objects from the DB
List<Show> allShows = showRepository.findAll();
Show semi1 = allShows.stream().filter(s -> s.getType() == Show.ShowType.SEMI1).findFirst().orElseThrow();
Show semi2 = allShows.stream().filter(s -> s.getType() == Show.ShowType.SEMI2).findFirst().orElseThrow();
Show grandFinal = allShows.stream().filter(s -> s.getType() == Show.ShowType.FINAL).findFirst().orElseThrow();

// 2. Fetch all Songs
List<Song> allSongs = songRepository.findAll();

System.out.println(">> Synchronizing Song-Show assignments...");

// 3. Loop through every song and decide where it belongs
for (int i = 0; i < allSongs.size(); i++) {
    Song song = allSongs.get(i);
    String countryName = song.getCountry().getName();
    boolean isDirectFinalist = song.getCountry().isBigFive() || countryName.equals("Austria");

    if (isDirectFinalist) {
        // Assign to Grand Final
        if (!song.getShows().contains(grandFinal)) {
            song.getShows().add(grandFinal);
            songRepository.save(song);
        }
    } else {
        // Assign to Semis (Alternating distribution: Even index to Semi 1, Odd to Semi 2)
        Show targetSemi = (i % 2 == 0) ? semi1 : semi2;
        
        if (!song.getShows().contains(targetSemi)) {
            song.getShows().add(targetSemi);
            songRepository.save(song);
        }
    }
}
System.out.println(">> 2026 Contestants assigned to Semi 1, Semi 2, and Final!");


        // STEP 5: Initialize Voters 
        if (voterRepository.count() == 0) { 
            System.out.println(">> Recruiting 2026 Juries and Public Voters...");
            List<Country> allCountries = countryRepository.findAll();

            for (Country c : allCountries) {
                // 1. Create a Professional Juror
                Jury j = new Jury();
                j.setName(c.getName() + " Judge");
                j.setOriginCountry(c);
                j.setJobTitle("Music Professional");
                voterRepository.save(j);

                // 2. Create a Public Voter 
                
                Voter v = new Voter(); 
                v.setName(c.getName() + " Public");
                v.setOriginCountry(c);
                voterRepository.save(v);
            }
        }
    }

    private void saveCountry(String name, boolean isBigFive, boolean isVirtual) {
        Country c = new Country();
        c.setName(name);
        c.setBigFive(isBigFive);
        c.setVirtualGroup(isVirtual);
        countryRepository.save(c);
    }
}
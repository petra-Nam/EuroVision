
package com.example.Controller;

import com.example.model.Song;
import com.example.model.Show;
import com.example.repository.SongRepository;
import com.example.repository.ShowRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DemoController {

    private final SongRepository songRepository;
    private final ShowRepository showRepository;

    public DemoController(SongRepository songRepository, ShowRepository showRepository) {
        this.songRepository = songRepository;
        this.showRepository = showRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Song> songs = songRepository.findAll();
        List<Show> shows = showRepository.findAll();

        model.addAttribute("songs", songs);
        model.addAttribute("shows", shows);

        return "home"; // This maps to the Thymeleaf template "home.html"
    }
}
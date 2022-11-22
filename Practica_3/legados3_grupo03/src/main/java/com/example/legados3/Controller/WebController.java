package com.example.legados3.Controller;

import com.example.legados3.Juego;
import com.example.legados3.Service.WebService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WebController {

    private final WebService webService;

    public WebController(WebService webService){
        this.webService = webService;
    }

    @GetMapping(path = "/")
    public String getHome() {
        return webService.totalGames();
    }

    @GetMapping(path = "/getTitle/{title}")
    public String getTitle(@PathVariable String title) {
        return webService.listDatos(title);
    }

    @GetMapping(path = "/list/{cinta}")
    public ArrayList<Juego> getList(@PathVariable String cinta) {
        return webService.list(cinta);
    }
}

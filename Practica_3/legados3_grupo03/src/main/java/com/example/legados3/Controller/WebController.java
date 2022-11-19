package com.example.legados3.Controller;

import com.example.legados3.Service.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class WebController {

    private final WebService webService;

    public WebController(WebService webService){
        this.webService = webService;
    }


    @GetMapping(path = "/")
    public String getHome(Model model) {
        model.addAttribute("numGames", webService.totalGames());
        return "index";
    }

    @GetMapping(path = "/listTitle")
    public String getListTitle(Model model, @RequestParam(name = "nombre") String nombre) {
        model.addAttribute("numGames", webService.totalGames());
        model.addAttribute("datosJuego", webService.listDatos(nombre));
        return "index";
    }

    @GetMapping(path = "/list")
    public String getList(Model model, @RequestParam(name = "cinta") String cinta) {
        model.addAttribute("datosLista", webService.list(cinta));
        return "list";
    }

}

package mbd.controller.login.municipio;


import mbd.infrastructure.service.municipio.TitulosCreditoService;
import mbd.model.municipio.TitulosCredito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
public class TitulosCreditoController {

    @Autowired
    TitulosCreditoService titulosCreditoService;


    @GetMapping("/titulos_credito/cedula/{cedula}")
    @ResponseStatus(HttpStatus.OK)
    public List<TitulosCredito> getTitulosByCedula(@PathVariable String cedula){
        List<TitulosCredito> titulosCreditos = new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoService.getTitulosByCedula(cedula);
        return titulosCreditos;
    }

    @GetMapping("/titulos_credito/clave/{clave}")
    @ResponseStatus(HttpStatus.OK)
    public List<TitulosCredito> getTitulosByClave(@PathVariable String clave){
        List<TitulosCredito> titulosCreditos = new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoService.getTitulosByClave(clave);
        return titulosCreditos;
    }
}

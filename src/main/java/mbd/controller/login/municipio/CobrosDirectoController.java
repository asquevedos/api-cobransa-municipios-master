package mbd.controller.login.municipio;


import mbd.infrastructure.repository.municipio.CobrosDirectosRepository;
import mbd.model.municipio.CobrosDirectos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
public class CobrosDirectoController {

    @Autowired
    CobrosDirectosRepository cobrosDirectoRepository;


    @GetMapping("/cobros_directo/cedula/{cedula}")
    @ResponseStatus(HttpStatus.OK)
    public List<CobrosDirectos> getCobrosDirectoByCedula(@PathVariable String cedula){
        List<CobrosDirectos> cobrosDirectos = new ArrayList<CobrosDirectos>();
        cobrosDirectos = cobrosDirectoRepository.findCobrosDirectoBycontribuyente(cedula);
        return cobrosDirectos;
    }

}

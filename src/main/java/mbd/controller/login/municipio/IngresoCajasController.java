package mbd.controller.login.municipio;

import mbd.infrastructure.repository.municipio.IngresosCajasRepository;
import mbd.model.municipio.IngresosCajas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
public class IngresoCajasController {

    @Autowired
    IngresosCajasRepository ingresosCajasRepository;



    @GetMapping("/ingresos_caja/{fecha}")
    @ResponseStatus(HttpStatus.OK)
    public List<IngresosCajas> getIngresoCajaByFecha(@PathVariable String fecha){
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            String usuario = securityContext.getAuthentication().getName();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(fecha);
            return ingresosCajasRepository.findByIngrecaudadorAndFecha(usuario,date);
        }catch (Exception e){
            return null;
        }

    }

}

package mbd.controller.login.municipio;


import mbd.infrastructure.repository.municipio.CobrosDirectosRepository;
import mbd.infrastructure.repository.municipio.IngresosCajasRepository;
import mbd.model.municipio.CobrosDirectos;
import mbd.model.municipio.IngresosCajas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api")
public class CobrosDirectoController {

    @Autowired
    CobrosDirectosRepository cobrosDirectoRepository;

    @Autowired
    IngresosCajasRepository ingresosCajasRepository;


    @GetMapping("/cobros_directo/cedula/{cedula}")
    @ResponseStatus(HttpStatus.OK)
    public List<CobrosDirectos> getCobrosDirectoByCedula(@PathVariable String cedula){
        List<CobrosDirectos> cobrosDirectos = new ArrayList<CobrosDirectos>();
        cobrosDirectos = cobrosDirectoRepository.findCobrosDirectoBycontribuyente(cedula);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        System.out.println("sdadasdsadasdasd"+securityContext.getAuthentication().getName());

        return cobrosDirectos;
    }

    @PostMapping("/cobros_directo/{cbrsecuencia}")
    public ResponseEntity<?> saveCobroDirecto(@RequestBody Integer cbrsecuencia, BindingResult result) {
        //aparentemente esto me saca el nombre del usuario logeado
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String usuario = securityContext.getAuthentication().getName();
        //System
        CobrosDirectos cobrosDirectosGrabar = new CobrosDirectos();
        IngresosCajas ingresoCajas= new IngresosCajas();
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Los errores son", errores);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            cobrosDirectosGrabar = cobrosDirectoRepository.findById(cbrsecuencia).get();
            //int lastCode= ingresosCajasRepository.findTopByOrderByIdDesc().getIngcodigo()+1;
            //ingresoCajas.setIngcodigo(lastCode); //Generar Código
            ingresoCajas.setIngfecha(new Date());
            ingresoCajas.setIngconcepto("COBROS DIRECTOS");
            ingresoCajas.setIngconcepto(cobrosDirectosGrabar.getCbrsecuencia()+"");
            ingresoCajas.setIngcobrado(84.2);//Esto hay que pedir
            ingresoCajas.setIngcobradoletras("");//Generar metodo para cambiar numeros a letra
            ingresoCajas.setIngvalor(84.2);//esto hay que pedir
            ingresoCajas.setIngvalorletras("");//Generar metodo para cambiar
            ingresoCajas.setInginteres(0.0);//Esto hay que generar
            ingresoCajas.setInginteresletras("");//cambiar los intereses a letras
            ingresoCajas.setIngrecargos(0.0);//preguntar Jhonny
            ingresoCajas.setIngrecargosletras("");//cambiar cargoa a letras
            ingresoCajas.setIngvuelto(0.0);
            ingresoCajas.setIngvueltoletras("CERO");
            ingresoCajas.setIngtotalpagar(84.2);//Revizar
            ingresoCajas.setIngtotalletras("");//generar
            ingresoCajas.setIngforma("PAGO BANCO");
            ingresoCajas.setIngcuenta("banco");//cuenta banco
            ingresoCajas.setIngbanco(usuario);//Sacar este dato del token de logeo
            ingresoCajas.setIngcheque("");
            ingresoCajas.setIngobservaciones("por concepto de: COBROS DIRECTOS");
            ingresoCajas.setIngrecaudador("");//Sacar del token de logeo

            ingresosCajasRepository.save(ingresoCajas);
            cobrosDirectosGrabar.setIngcodigo(ingresoCajas);
            cobrosDirectoRepository.save(cobrosDirectosGrabar);

            //personaGrabar = personaRepositorio.save(persona);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el inserción en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La transacción se ha generado con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

}

package mbd.controller.login.municipio;


import com.fasterxml.jackson.databind.JsonNode;
import mbd.infrastructure.repository.municipio.IngresosCajasRepository;
import mbd.infrastructure.repository.municipio.TitulosCreditoRepository;
import mbd.infrastructure.service.municipio.TitulosCreditoService;
import mbd.model.municipio.*;
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
public class TitulosCreditoController {

    @Autowired
    TitulosCreditoService titulosCreditoService;
    @Autowired
    TitulosCreditoRepository titulosCreditoRepository;



    @GetMapping("/titulos_credito/cedula/{cedula}")
    @ResponseStatus(HttpStatus.OK)
    public List<TitulosCredito> getTitulosByCedula(@PathVariable String cedula){
        List<TitulosCredito> titulosCreditos = new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoService.getTitulosByCedula(cedula);
        return titulosCreditos;
    }

    @GetMapping("/titulos_credito/reporte/{cedula}/{anio}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReporteComprobante> getTitulosReporte(@PathVariable String cedula, @PathVariable Integer anio){
        List<TitulosCredito> titulosCreditos = new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoRepository.findByCrdcontribuyentePagado(cedula,anio);
        List<ReporteComprobante> reportesComprobantes= new ArrayList<ReporteComprobante>();

        //generate a map grouping by ingcodigo all titulos credito
        Map<IngresosCajas, List<TitulosCredito>> titulosByIngreso = titulosCreditos.stream().collect(Collectors.groupingBy(TitulosCredito::getIngcodigo));
        //iterate the map titulosByIngreso
        for (Map.Entry<IngresosCajas,  List<TitulosCredito>> entry : titulosByIngreso.entrySet()) {
            ReporteComprobante reporteComprobante = new ReporteComprobante();
            reporteComprobante.setNumeroComprobante(entry.getKey().getIngcodigo());
            reporteComprobante.setFechaPago(entry.getKey().getIngfecha());
            reporteComprobante.setContribuyente(entry.getValue().get(0).getCrdcontribuyente());
            IngresosCajas key = entry.getKey();
            for(TitulosCredito tc: entry.getValue())
            {
                reporteComprobante.getDetallesPago().add(tc.getCrddetalle());
            }
            reporteComprobante.setValorTotalPagar(key.getIngtotalpagar());
            reportesComprobantes.add(reporteComprobante);
        }


        return reportesComprobantes;
    }

    @GetMapping("/titulos_credito/clave/{clave}")
    @ResponseStatus(HttpStatus.OK)
    public List<TitulosCredito> getTitulosByClave(@PathVariable String clave){
        List<TitulosCredito> titulosCreditos = new ArrayList<TitulosCredito>();
        //titulosCreditos = titulosCreditoService.getTitulosByClave(clave);
        return titulosCreditos;
    }


    @GetMapping("/titulos_credito/conceptobycedula")
    @ResponseStatus(HttpStatus.OK)

    //@RequestBody JsonNode data,
    //public ReporteTitulosCredito getTitulosByCedulaAndConcepto(@PathVariable String cedula, @PathVariable String concepto, @PathVariable Integer anio, ){
    public ReporteTitulosCredito getTitulosByCedulaAndConcepto(@RequestBody JsonNode data ){
        String cedula=data.get("cedula").asText();
        String concepto=data.get("concepto").asText();
        Integer anio= data.get("anio").asInt();
        boolean discapacidad=data.get("aplicaDiscapacidad").asBoolean();
        boolean terceraEdad=data.get("aplicaTerceraEdad").asBoolean();
        boolean institucionPublica=data.get("aplicaInstitucionPublica").asBoolean();
        ReporteTitulosCredito reporteTitulosCredito= new ReporteTitulosCredito();
        List<TitulosCredito> titulosCreditos = new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoService.getTitulosByCedulaAndConcepto(cedula,concepto,anio);
        reporteTitulosCredito.setTitulosCreditos(titulosCreditos);
        reporteTitulosCredito.setAplicaDiscapacidad(discapacidad);
        reporteTitulosCredito.setAplicaTerceraEdad(terceraEdad);
        reporteTitulosCredito.setAplicaInstitucionPublica(institucionPublica);
        reporteTitulosCredito.generateSummary();
        return reporteTitulosCredito;
    }

    @GetMapping("/conceptos/titulos_credito/cedula/{cedula}")
    @ResponseStatus(HttpStatus.OK)
    public List<ResumenDeuda> getConceptosByCedula(@PathVariable String cedula){
        List<ResumenDeuda> resumenDeuda = new ArrayList<ResumenDeuda>();

        for (String d : titulosCreditoRepository.findConceptosByCrdcontribuyente(cedula))
        {
            String[] partes = d.split(",");
            String conceptoExtract = partes[0];
            String anio = partes[1];
            String fingreso = partes[2];
            String femision = partes[3];
            ResumenDeuda r = new ResumenDeuda();
            r.setConcepto(conceptoExtract);
            r.setAnio(Integer.parseInt(anio));
            r.setPropietario(cedula);
            r.setFechaIngreso(fingreso);
            r.setFechaEmision(femision);

            resumenDeuda.add(r);
        }
        //ordenar resumen deuda por anio de menor a mator
        resumenDeuda.sort((o1, o2) -> o1.getAnio().compareTo(o2.getAnio()));
        //resumenDeuda.sort((o1, o2) -> o2.getAnio().compareTo(o1.getAnio()));

        return resumenDeuda;
    }


    //Recuerda que tienes que grabar todos los titulos de credito que se generan en el sistema,, entonces hay que hacer el filtro bien
    @PostMapping("/titulos_credito/pagar")
    public ResponseEntity<?> saveTitulosCredito(@RequestBody JsonNode data, BindingResult result) {


        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Los errores son", errores);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            titulosCreditoService.grabarTitulosCredito(
                    data.get("cedula").asText(),
                    data.get("concepto").asText(),
                    data.get("anio").asInt(),
                    data.get("aplicaDiscapacidad").asBoolean(),
                    data.get("aplicaTerceraEdad").asBoolean(),
                    data.get("aplicaInstitucionPublica").asBoolean()
            );

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

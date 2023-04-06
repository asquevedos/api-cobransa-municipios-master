package mbd.infrastructure.service.municipio;

import mbd.infrastructure.repository.municipio.ConceptoIngresosRepository;
import mbd.infrastructure.repository.municipio.EcobrasRepository;
import mbd.infrastructure.repository.municipio.InteresesRepository;
import mbd.infrastructure.repository.municipio.TitulosCreditoRepository;
import mbd.model.municipio.ConceptoIngresos;
import mbd.model.municipio.Descuento;
import mbd.model.municipio.Ecobras;
import mbd.model.municipio.TitulosCredito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class TitulosCreditoService {

    @Autowired
    InteresesRepository interesesRepository;
    @Autowired
    TitulosCreditoRepository titulosCreditoRepository;
    @Autowired
    ConceptoIngresosRepository conceptoIngresosRepository;

    @Autowired
    EcobrasRepository ecobrasRepository;

    public TitulosCredito calcularIntereses(TitulosCredito tituloCredito) {

           SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy");
           SimpleDateFormat formatter_month = new SimpleDateFormat("MM");
           int anioEmisionTituloCredito = Integer.parseInt(formatter_year.format(tituloCredito.getCrdfecemision()));
           int mesEmisionTituloCredito = Integer.parseInt(formatter_month.format(tituloCredito.getCrdfecemision()));
           int anioActual = Integer.parseInt(formatter_year.format(new Date()));
           int mesActual = Integer.parseInt(formatter_month.format(new Date()));

           String conceptoTituloCredito = tituloCredito.getCrdconcepto();

           ConceptoIngresos conceptoIngreso = conceptoIngresosRepository.findConceptoIngresosBy(conceptoTituloCredito);

           int diasConceptoIngresoSinIntereses = conceptoIngreso.getCptdiaslibres();
           if(new Date().compareTo(tituloCredito.getCrdfecemision() )>0)
           {
               long diffInMillies = Math.abs(new Date().getTime() - tituloCredito.getCrdfecemision().getTime());
               int diasMora = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
               if (diasConceptoIngresoSinIntereses > diasMora) {  //Ojo preguntar al Jhonny si aqui es mayor o igual o solo mayor

                   int secuencia = interesesRepository.findInteresesBy(anioEmisionTituloCredito, mesEmisionTituloCredito).getSecuencia();
                   Double interesesAcumulados = interesesRepository.findSumAllIntereses(secuencia);
                   Double interes = (interesesAcumulados * tituloCredito.getCrdvalor());
                   tituloCredito.setCrdinteres(interes);
               }
           }

        return tituloCredito;
    }

    //Metodo para sacar los descuentos por concepto de contribucion especial de mejoras
    public TitulosCredito calcularValorNoRembolsableContMejoras(TitulosCredito tituloCredito) {

        //con esto saco del objeto Ecobras el valor de la contribucion especial de mejoras y su descuento.
        String crdReferencia = tituloCredito.getCrdreferencia();
        //verify if the crdReferencia is a number and generate a catch if it is not a number
        try {

            Ecobras ecobras= ecobrasRepository.findById(Double.parseDouble(crdReferencia)).get();
            //Ojo ya tengo la parte no rembolsable. Posiblemente este en un valor transitorio que utilizo para mostrar los datos
            Double parteNoRembolsable= tituloCredito.getCrdvalor()-(tituloCredito.getCrdvalor()*ecobras.generatePorcentajeDescuento());
            tituloCredito.setValorConNoRembolsable(parteNoRembolsable);
        } catch (NumberFormatException e) {
            return tituloCredito;
        }



        return tituloCredito;
    }

    public TitulosCredito calcularDescuentos(TitulosCredito tituloCredito, List<String> listaDescuentos)
    {
        Calendar fechaEmision = Calendar.getInstance();
        fechaEmision.setTime(tituloCredito.getCrdfecemision());
        Calendar fechaActual = Calendar.getInstance();
        Descuento descuentoProntoPago = new Descuento();
        List<Descuento> descuentosEspecialesAplicados = new ArrayList<>();
        int sumaDescuentos=20; //esto por que a los titulos de credito se les descuenta el 20% de intereses de manera general a todos
        for (String descuento:listaDescuentos) {
            Descuento descuentoAplicado = new Descuento();
            if (descuento.equals("TERCERA_EDAD")) {
                descuentoAplicado.setMotivo("Descuento por Tercera Edad");
                descuentoAplicado.setValor(50.0);
            }
            if (descuento.equals("DISCAPACIDAD")) {
                descuentoAplicado.setMotivo("Descuento por Tercera Edad");
                descuentoAplicado.setValor(50.0);
            }
            if (descuento.equals("ENTIDADES_PUBLICAS")) {
                descuentoAplicado.setMotivo("Descuento por Tercera Edad");
                descuentoAplicado.setValor(50.0);
            }
            descuentosEspecialesAplicados.add(descuentoAplicado);
        }
        //TODO: me falta esto o en el caso que el pago se de contado de la totalidad de la contribución

        if (fechaEmision.get(Calendar.MONTH) ==  fechaActual.get(Calendar.MONTH)
                && fechaEmision.get(Calendar.YEAR) == fechaActual.get(Calendar.YEAR)) {

            descuentoProntoPago.setMotivo("Primer mes de la emisión");
            descuentoProntoPago.setValor(10.0);
            descuentosEspecialesAplicados.add(descuentoProntoPago);
        }
        tituloCredito.setDescuentosAplicados(descuentosEspecialesAplicados);
        return tituloCredito;
    }

    //TODO: Con este metodo saco todos los titulos de credito pero ojo que no sirve por que hay que sacar los descuento de forma personalizada
    public List<TitulosCredito> getTitulosByClave(String clave){
        List<TitulosCredito> titulosCreditos =  new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoRepository.findByClaveCatastral(clave);
        for (TitulosCredito tituloCredito: titulosCreditos) {
            tituloCredito = calcularIntereses(tituloCredito);
            if(tituloCredito.getCrdconcepto().equals("CONTRIBUCION ESPECIAL POR MEJORAS"))
            {
                tituloCredito = calcularValorNoRembolsableContMejoras(tituloCredito);
                tituloCredito= calcularDescuentos(tituloCredito,null);
                tituloCredito = calcularIntereses(tituloCredito);
            }
        }
        return titulosCreditos;
    }

    public List<TitulosCredito> getTitulosByCedula(String cedula){
        List<TitulosCredito> titulosCreditos =  new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoRepository.findByCrdcontribuyente(cedula);
        for (TitulosCredito tituloCredito: titulosCreditos) {
            tituloCredito = calcularIntereses(tituloCredito);
        }
        return titulosCreditos;
    }
}

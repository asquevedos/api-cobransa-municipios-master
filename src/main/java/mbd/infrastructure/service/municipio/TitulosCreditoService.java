package mbd.infrastructure.service.municipio;

import mbd.infrastructure.repository.municipio.*;
import mbd.model.municipio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    IngresosCajasRepository ingresosCajasRepository;

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
               //sum one month to the date

               long diffInMillies = Math.abs(new Date().getTime() - tituloCredito.getCrdfecemision().getTime());
               int diasMora = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
               //Si esta bien. "OJO es <" El Jhonny en algunos datos le setea con un valor alto para no cobrar intereses.
               //OJO con esto
               if (diasConceptoIngresoSinIntereses < diasMora) {  //Ojo preguntar al Jhonny si aqui es mayor o igual o solo mayor

                   int secuencia = interesesRepository.findInteresesBy(anioEmisionTituloCredito, mesEmisionTituloCredito).getSecuencia();
                   //ojo el interes se calcula justo en el d'ia de la emision, ahorita no estoy considerando eso.
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
            double porcentajeCEM=ecobras.generatePorcentajeDescuento();
            Double parteNoRembolsable= tituloCredito.getCrdvalor()*porcentajeCEM/100;
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
        //tituloCredito.setDescuentosAplicados(descuentosEspecialesAplicados);
        return tituloCredito;
    }


    public List<TitulosCredito> getTitulosByCedulaAndConcepto(String cedula, String concepto, Integer anio){
        List<TitulosCredito> titulosCreditos =  new ArrayList<TitulosCredito>();
        titulosCreditos = titulosCreditoRepository.findByContribuyenteAndConcepto(cedula, concepto, anio);
        for (TitulosCredito tituloCredito: titulosCreditos) {
            //tituloCredito = calcularIntereses(tituloCredito);
            if(tituloCredito.getCrdconcepto().equals("CONTRIBUCION ESPECIAL POR MEJORAS"))
            {
                tituloCredito = calcularValorNoRembolsableContMejoras(tituloCredito);
                List<String> descuento = new ArrayList<>();
                descuento.add("Tercera Edad");
                tituloCredito= calcularDescuentos(tituloCredito,descuento);
                tituloCredito = calcularIntereses(tituloCredito);
            }
            else{
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

    @Transactional
    public boolean grabarTitulosCredito(String cedula, String concepto, Integer anio,boolean aplicaDiscapacidad,boolean aplicaTerceraEdad, boolean aplicaInstitucionPublica){
        //aparentemente esto me saca el nombre del usuario logeado
        try {


        List<TitulosCredito> titulosCreditos=getTitulosByCedulaAndConcepto(cedula, concepto, anio);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String usuario = securityContext.getAuthentication().getName();

        Integer lastid=ingresosCajasRepository.lastID();

            ReporteTitulosCredito reporteTitulosCredito= new ReporteTitulosCredito();
            reporteTitulosCredito.setTitulosCreditos(titulosCreditos);
            reporteTitulosCredito.setAplicaTerceraEdad(aplicaTerceraEdad);
            reporteTitulosCredito.setAplicaDiscapacidad(aplicaDiscapacidad);
            reporteTitulosCredito.setAplicaInstitucionPublica(aplicaInstitucionPublica);
            reporteTitulosCredito.generateSummary();

            IngresosCajas ingresoCajas= new IngresosCajas();
            ingresoCajas.setIngfecha(new Date());
            ingresoCajas.setIngconcepto("TITULOS DE CREDITO");
            ingresoCajas.setIngcobrado(reporteTitulosCredito.getValorTotalPagar());
            ingresoCajas.setIngcobradoletras(convertirDoubleALetras(reporteTitulosCredito.getValorTotalPagar()));
            ingresoCajas.setIngreferencia(titulosCreditos.get(0).getCrdcodigo());///esto esta mal no se de donde sacar este codigo el Jhonny quedo en ver
            ingresoCajas.setIngvalor(reporteTitulosCredito.getValorReferencialPagar());//esto sin descuentos ni recargos
            ingresoCajas.setIngvalorletras(convertirDoubleALetras(reporteTitulosCredito.getValorReferencialPagar()));//Generar metodo para cambiar
            ingresoCajas.setInginteres(reporteTitulosCredito.getValorTotalInteres());//valor interes
            ingresoCajas.setInginteresletras(convertirDoubleALetras(reporteTitulosCredito.getValorTotalInteres()));//cambiar los intereses a letras
            ingresoCajas.setIngrecargos(0.0);
            ingresoCajas.setIngrecargosletras("");
            ingresoCajas.setIngvuelto(0.0);
            ingresoCajas.setIngvueltoletras("CERO");
            ingresoCajas.setIngtotalpagar(reporteTitulosCredito.getValorTotalPagar());//Revizar
            ingresoCajas.setIngtotalletras(convertirDoubleALetras(reporteTitulosCredito.getValorTotalPagar()));//generar
            ingresoCajas.setIngforma("PAGO BANCO");
            ingresoCajas.setIngcuenta("banco");//cuenta banco
            ingresoCajas.setIngbanco(usuario);//Sacar este dato del token de logeo
            ingresoCajas.setIngcheque("");
            ingresoCajas.setIngobservaciones("por concepto de: TITULOS DE CREDITO UTILIZANDO SISTEMA BANCARIO");
            ingresoCajas.setIngrecaudador(usuario);//Sacar del token de logeo

            ingresoCajas.setIngcodigo(lastid);
            ingresoCajas=ingresosCajasRepository.save(ingresoCajas);
        for (TitulosCredito tituloCredito: titulosCreditos) {

            tituloCredito.setIngcodigo(ingresoCajas);
            titulosCreditoRepository.save(tituloCredito);
        }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String convertirDoubleALetras(double numero) {
        long parteEntera = (long) numero;
        long parteDecimal = (long) ((numero - parteEntera) * 100);

        String letrasParteEntera = convertirNumeroALetras((int) parteEntera);
        String letrasParteDecimal = convertirNumeroALetras((int) parteDecimal);

        String letras = letrasParteEntera + " CON " + letrasParteDecimal + " CENTAVOS";
        return letras.toUpperCase();
    }

    public static String convertirNumeroALetras(int numero) {
        String[] unidades = {"", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"};
        String[] decenas = {"", "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
        String[] especiales = {"", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"};

        String letras = "";

        if (numero == 0) {
            letras = "CERO";
        } else if (numero < 0) {
            letras = "MENOS " + convertirNumeroALetras(-numero);
        } else if (numero < 10) {
            letras = unidades[numero];
        } else if (numero < 20) {
            letras = especiales[numero - 10];
        } else if (numero < 100) {
            letras = decenas[numero / 10] + " " + unidades[numero % 10];
        } else if (numero < 1000) {
            letras = unidades[numero / 100] + " CIENTOS " + convertirNumeroALetras(numero % 100);
        } else if (numero < 1000000) {
            letras = convertirNumeroALetras(numero / 1000) + " MIL " + convertirNumeroALetras(numero % 1000);
        } else if (numero < 1000000000) {
            letras = convertirNumeroALetras(numero / 1000000) + " MILLONES " + convertirNumeroALetras(numero % 1000000);
        }

        return letras.toUpperCase();
    }

}

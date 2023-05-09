package mbd.model.municipio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReporteTitulosCredito {

    private double valorReferencialPagar;
    private double valorTotalDescuento;
    private double valorTotalInteres;
    private double valorTotalPagar;

    boolean aplicaDiscapacidad=false;
    boolean aplicaTerceraEdad=false;
    boolean aplicaInstitucionPublica=false;
    private List<TitulosCredito> titulosCreditos=new ArrayList<TitulosCredito>();

    public void generateSummary(){
        for (TitulosCredito tituloCredito:titulosCreditos) {
            this.valorReferencialPagar +=tituloCredito.getCrdvalor();
            tituloCredito=descuentos(tituloCredito);
            this.valorTotalDescuento+=tituloCredito.getDescuentosValor();
            this.valorTotalInteres+=tituloCredito.getCrdinteres();

        }
        this.valorTotalPagar= (this.valorReferencialPagar+this.valorTotalInteres)-this.valorTotalDescuento;
    }
    public TitulosCredito descuentos(TitulosCredito tituloCredito)
    {
        double descuentoPorcentaje =20.0; //todos empiezan con un 20% de descuento
        double descuentoValor=0.0;
        //descuento del 10% si es que el contribuyevnte paga dentro del primer mes de la emision getCrdfecemision del titulo de creditos
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tituloCredito.getCrdfecemision());
        calendar.add(Calendar.DATE, 31);
        Date fechaEmisionMasUnMes = calendar.getTime();
        if(new Date().before(fechaEmisionMasUnMes))
        {
            descuentoPorcentaje+=10.0;
        }

        if (aplicaDiscapacidad==true || aplicaTerceraEdad==true || aplicaInstitucionPublica==true){
            descuentoPorcentaje = 50.0;
        }
        descuentoValor=tituloCredito.getValorConNoRembolsable()*descuentoPorcentaje/100;
        tituloCredito.setDescuentosPorcentaje(descuentoPorcentaje);
        tituloCredito.setDescuentosValor(descuentoValor);
        return tituloCredito;
    }
}

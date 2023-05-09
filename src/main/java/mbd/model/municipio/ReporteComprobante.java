package mbd.model.municipio;

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
public class ReporteComprobante {

    int numeroComprobante;
    Date fechaPago;
    String recaudador;
    Contribuyente contribuyente;
    List<String> detallesPago = new ArrayList<>();
    double valorTotalPagar;


}

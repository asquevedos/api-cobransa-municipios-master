package mbd.model.municipio;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResumenDeuda {

    String concepto;
    Integer anio;
    String propietario;
    String fechaIngreso;
    String fechaEmision;

}

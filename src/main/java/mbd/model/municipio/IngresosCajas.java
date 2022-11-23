package mbd.model.municipio;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ingresoscaja")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngresosCajas {

    @Id
    private Integer ingcodigo;

    private Date ingfecha;
    private String ingconcepto;
    private String ingreferencia;
    private Double ingcobrado;
    private String ingcobradoletras;
    private Double precision;
    private String ingvalorletras;
    private Double ingvalor;
    private String inginteresletras;
    private Double ingrecargos;
    private String ingrecargosletras;
    private Double ingvuelto;
    private String ingvueltoletras;
    private Double ingtotalpagar;
    private String ingtotalletras;
    private String ingforma;
    private String ingcuenta;
    private String ingbanco;
    private String ingcheque;
    private String ingobservaciones;
    private String ingrecaudador;

}

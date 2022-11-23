package mbd.model.municipio;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cobrosdirectos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CobrosDirectos {


    @Id
    private Integer cbrsecuencia;

    private Date cbrfecha;
    private String cbrmotivo;
    private String cbrdetalle;
    private Double cbrvalor;

    @ManyToOne
    @JoinColumn(name = "cbrcontribuyente")
    private Contribuyente cbrcontribuyente;
    private String cbrdepartamento;
    private String cbrusuario;
    private String cbrrecaudador;
    private Integer cbrdocumento;
    private Integer cbrimpreso;
    private String cbrvalorletras;
    private int ingcodigo;
    private String cbrubicalocal;
    private String cbrubicageografica;
    private String cbrestado;

}

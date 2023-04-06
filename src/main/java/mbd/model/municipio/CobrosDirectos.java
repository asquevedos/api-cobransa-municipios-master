package mbd.model.municipio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties(value={"contribuyente", "hibernateLazyInitializer", "handler"}, allowSetters=true)
    private Contribuyente cbrcontribuyente;
    private String cbrdepartamento;
    private String cbrusuario;
    private String cbrrecaudador;
    private Integer cbrdocumento;
    private Integer cbrimpreso;
    private String cbrvalorletras;


    @ManyToOne
    @JoinColumn(name = "ingcodigo")
    @JsonBackReference
    private IngresosCajas ingcodigo;


    private String cbrubicalocal;
    private String cbrubicageografica;
    private String cbrestado;

}

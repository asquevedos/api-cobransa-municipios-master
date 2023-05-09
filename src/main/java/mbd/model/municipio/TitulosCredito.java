package mbd.model.municipio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;



import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tituloscredito")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TitulosCredito {
    @Id
    private String crdcodigo;
    @JsonIgnore
    private String crdsecundario;
    @JsonIgnore
    private int crdanio;
    @JsonIgnore
    private int crdmes;

    private Date crdfecemision;
    private String crddetalle;
    @JsonIgnore
    private String crdreferencia;
    @JsonIgnore
    private String crdconcepto;
    @JsonIgnore
    private double crdvalor;
    @JsonIgnore
    private int crdsecuencia;

    private int crdcuotas;
    @JsonIgnore
    private int crdpagado;
    @JsonIgnore
    private int crdverificado;

    @ManyToOne
    @JoinColumn(name = "crdcontribuyente")
    private Contribuyente crdcontribuyente;



    private double crdinteres;
    private Date crdfechacobro;
    private String crdrecaudador;
    @JsonIgnore
    private String crdrefrendador;
    @JsonIgnore
    private String crdresponsable;
    @JsonIgnore
    private String crdespecifico;

    private String crdvalorletras;

    @ManyToOne
    @JoinColumn(name = "ingcodigo")
    @JsonBackReference
    private IngresosCajas ingcodigo;

    private int crdnumtitulo;
    @JsonIgnore
    private Date crdfecingreso;
    @JsonIgnore
    private Date crdfecverificado;
    @JsonIgnore
    private double crdvalor1;
    @JsonIgnore
    private String crdubicalocal;
    @JsonIgnore
    private String crdubicageografica;
    @JsonIgnore
    private String crdestado;
    @JsonIgnore
    private int crdnotificacion;
    @JsonIgnore
    private String cedulant;
    @JsonIgnore
    private Date crdfechacoactiva;
    @JsonIgnore
    private int crdcoactiva;

    @JsonIgnore
    @Transient
    private double valorConNoRembolsable;

    @JsonIgnore
    @Transient
    private double descuentosPorcentaje;

    @JsonIgnore
    @Transient
    private double descuentosValor;

}

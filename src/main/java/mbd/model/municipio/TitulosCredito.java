package mbd.model.municipio;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String crdsecundario;
    private int crdanio;
    private int crdmes;
    private Date crdfecemision;
    private String crddetalle;
    private String crdreferencia;
    private String crdconcepto;
    private double crdvalor;
    private int crdsecuencia;
    private int crdcuotas;
    private int crdpagado;
    private int crdverificado;

    @ManyToOne
    @JoinColumn(name = "crdcontribuyente")
    private Contribuyente crdcontribuyente;


    private double crdinteres;
    private Date crdfechacobro;
    private String crdrecaudador;
    private String crdrefrendador;
    private String crdresponsable;
    private String crdespecifico;
    private String crdvalorletras;

    @ManyToOne
    @JoinColumn(name = "ingcodigo")
    @JsonBackReference
    private IngresosCajas ingcodigo;

    private int crdnumtitulo;
    private Date crdfecingreso;
    private Date crdfecverificado;
    private double crdvalor1;
    private String crdubicalocal;
    private String crdubicageografica;
    private String crdestado;
    private int crdnotificacion;
    private String cedulant;
    private Date crdfechacoactiva;
    private int crdcoactiva;

    @Transient
    private Double valorConNoRembolsable;

    @Transient
    private List<Descuento> descuentosAplicados;

}

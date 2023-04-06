package mbd.model.municipio;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "contribuyentes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contribuyente {

    @Id
    private String ctrcedula;

    private String ctrnombre;
    private String ctrdireccion;
    private String ctrtelefono;

    @JsonIgnore
    private Date ctrfecha;
    @JsonIgnore
    private String ctrusuario;
    @JsonIgnore
    private int ctrexonera;
    @JsonIgnore
    private String ctrrazonex;
    @JsonIgnore
    private String ctrestado;
    @JsonIgnore
    private String ctrcedulare;

    private String ctrciudad;
    @JsonIgnore
    private String ctrnumero;
    @JsonIgnore
    private int ctrtipo;

    @JsonIgnore
    private String ctrhora;
    @JsonIgnore
    private String ctresttra;
    @JsonIgnore
    private String cttipopro;

    private String ctremail;
    @JsonIgnore
    private String ctrtipoident;
    @JsonIgnore
    private String ctrcelular;
}

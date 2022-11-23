package mbd.model.municipio;


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
    private Date ctrfecha;
    private String ctrusuario;
    private int ctrexonera;
    private String ctrrazonex;
    private String ctrestado;
    private String ctrcedulare;
    private String ctrciudad;
    private String ctrnumero;
    private int ctrtipo;
    private String ctrhora;
    private String ctresttra;
    private String cttipopro;
    private String ctremail;
    private String ctrtipoident;
    private String ctrcelular;
}

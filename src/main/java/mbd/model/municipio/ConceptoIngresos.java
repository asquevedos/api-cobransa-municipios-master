package mbd.model.municipio;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "conceptosingresos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConceptoIngresos {


    @Id
    private Integer cptcodigo;

    private String cptdescripcion;
    private String cpttipo;

    private Integer cptsecuencia;

    private String cptformula;
    private String cptordenanza;
    private String cptaccion;
    private String cptvalores;
    private String cptformato;
    private String cptdetalle;
    private String cptcertificado;
    private String cpttimbres;

    private Integer cptdiaslibres;

    private String cpttablainteres;

    private Integer cptgrupo;
    private Integer cpttipotitulo;

    private String cpttributario;







}

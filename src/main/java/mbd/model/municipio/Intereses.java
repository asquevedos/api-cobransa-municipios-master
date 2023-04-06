package mbd.model.municipio;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "intereses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Intereses {


    @Id
    private Integer secuencia;

    private Integer anio;
    private Integer mes;

    private Double interes;
}

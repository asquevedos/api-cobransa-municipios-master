package mbd.model.municipio;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ecobras")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ecobras {

    @Id
    private Double eccodigo;

    private Double ecvalobr;
    private Double ecvalnr;

    public double generatePorcentajeDescuento() {

        return (ecvalnr*100)/(ecvalobr + ecvalnr);
        //return ecvalobr * 100 / (ecvalobr + ecvalnr);
    }

}

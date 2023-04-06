package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.IngresosCajas;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IngresosCajasRepository extends JpaRepository<IngresosCajas, Integer> {

    //IngresosCajas findTopByOrderByIdDesc();

}

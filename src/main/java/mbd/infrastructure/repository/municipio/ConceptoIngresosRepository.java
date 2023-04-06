package mbd.infrastructure.repository.municipio;


import mbd.model.municipio.ConceptoIngresos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConceptoIngresosRepository extends JpaRepository<ConceptoIngresos, Integer> {

    //find only one conceptoIngresos by cptdescripcion
    @Query("SELECT c FROM ConceptoIngresos c WHERE c.cptdescripcion = :cptdescripcion")
    ConceptoIngresos findConceptoIngresosBy(@Param("cptdescripcion") String cptdescripcion);



}

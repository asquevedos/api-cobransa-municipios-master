package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.IngresosCajas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface IngresosCajasRepository extends JpaRepository<IngresosCajas, Integer> {

    //IngresosCajas findTopByOrderByIdDesc();
    //METHOD FOR THE LAST ID IN NUMBER +1

    @Query(value = "SELECT MAX(ingcodigo)+1 FROM ingresoscaja", nativeQuery = true)
    Integer lastID();

    //all the ongresoscajas by ingrerecaudador and ingfecha is equal a date


    @Query("SELECT ic FROM IngresosCajas ic WHERE ic.ingrecaudador = :ingrecaudador and ic.ingfecha = :fecha")
    List<IngresosCajas> findByIngrecaudadorAndFecha(@Param("ingrecaudador") String ingrecaudador, @Param("fecha") Date fecha);








}

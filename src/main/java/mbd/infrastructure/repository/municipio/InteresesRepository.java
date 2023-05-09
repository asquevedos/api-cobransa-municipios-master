package mbd.infrastructure.repository.municipio;


import mbd.model.municipio.Intereses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InteresesRepository extends JpaRepository<Intereses, Integer> {

    @Query("SELECT i FROM Intereses i WHERE i.anio = :anio+1 AND i.mes = :mes")
    Intereses findInteresesBy(@Param("anio") Integer anio, @Param("mes") Integer mes);

    @Query("SELECT SUM(i.interes) FROM Intereses i WHERE i.secuencia >= :secuencia")
    Double findSumAllIntereses(@Param("secuencia") Integer secuencia);






}

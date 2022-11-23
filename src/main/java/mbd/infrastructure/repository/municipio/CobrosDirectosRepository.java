package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.CobrosDirectos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CobrosDirectosRepository extends JpaRepository<CobrosDirectos, Integer> {


    @Query("select cd from CobrosDirectos cd where cd.cbrcontribuyente.ctrcedula = :cedula and cd.ingcodigo = 0 ")
    List<CobrosDirectos> findCobrosDirectoBycontribuyente(@Param("cedula") String cedula);
}

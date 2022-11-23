package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.TitulosCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TitulosCreditoRepository extends JpaRepository<TitulosCredito, Integer> {


    @Query("select tc from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' ORDER BY tc.crdfecemision")
    List<TitulosCredito> findByCrdcontribuyente(@Param("cedula") String cedula);

    @Query("select tc from TitulosCredito tc where tc.crdsecundario = :clave and tc.crdpagado = 0 and tc.crdestado = 'ACTIVO' ORDER BY tc.crdfecemision")
    List<TitulosCredito> findByClaveCatastral(@Param("clave") String clave);
}

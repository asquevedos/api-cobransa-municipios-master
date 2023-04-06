package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.TitulosCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TitulosCreditoRepository extends JpaRepository<TitulosCredito, Integer> {


    @Query("select tc from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio <= YEAR(CURRENT_DATE) ORDER BY tc.crdanio")
    List<TitulosCredito> findByCrdcontribuyente(@Param("cedula") String cedula);

    @Query("select tc from TitulosCredito tc where tc.crdsecundario = :clave and tc.crdpagado = 0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio <= YEAR(CURRENT_DATE) ORDER BY tc.crdanio")
    List<TitulosCredito> findByClaveCatastral(@Param("clave") String clave);

    @Query("select tc from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio = YEAR(CURRENT_DATE) and tc.crdconcepto= :concepto ORDER BY tc.crdanio")
    List<TitulosCredito> findByCrdcontribuyente(@Param("cedula") String cedula, @Param("concepto") String concepto);

    //all conceprtos from titulos credito
    @Query("select distinct tc.crdconcepto from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio = YEAR(CURRENT_DATE) ORDER BY tc.crdanio")
    List<String> findConceptosByCrdcontribuyente(@Param("cedula") String cedula);


}

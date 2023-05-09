package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.TitulosCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TitulosCreditoRepository extends JpaRepository<TitulosCredito, Integer> {


    @Query("select tc from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio <= YEAR(CURRENT_DATE) ORDER BY tc.crdanio")
    List<TitulosCredito> findByCrdcontribuyente(@Param("cedula") String cedula);

    @Query("select tc from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula  and tc.crdanio = :anio and tc.ingcodigo <> 0 ORDER BY tc.crdanio")
    List<TitulosCredito> findByCrdcontribuyentePagado(@Param("cedula") String cedula,@Param("anio") Integer anio);

    @Query("select tc from TitulosCredito tc where tc.crdsecundario = :clave and tc.crdpagado = 0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio <= YEAR(CURRENT_DATE) ORDER BY tc.crdanio")
    List<TitulosCredito> findByClaveCatastral(@Param("clave") String clave);

    @Query("select tc from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0 and tc.crdanio = :anio and tc.crdconcepto= :concepto ORDER BY tc.crdanio")
    List<TitulosCredito> findByContribuyenteAndConcepto(@Param("cedula") String cedula, @Param("concepto") String concepto, @Param("anio") Integer anio);


    @Query("select distinct tc.crdconcepto, tc.crdanio,tc.crdfecingreso,tc.crdfecemision from TitulosCredito tc where tc.crdcontribuyente.ctrcedula = :cedula and tc.crdpagado =0 and tc.crdestado = 'ACTIVO' and tc.ingcodigo.ingcodigo=0  ")
    List<String> findConceptosByCrdcontribuyente(@Param("cedula") String cedula);


}

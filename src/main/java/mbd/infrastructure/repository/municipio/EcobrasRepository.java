package mbd.infrastructure.repository.municipio;

import mbd.model.municipio.CobrosDirectos;
import mbd.model.municipio.Ecobras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EcobrasRepository extends JpaRepository<Ecobras, Double> {



}

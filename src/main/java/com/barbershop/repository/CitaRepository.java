package com.barbershop.repository;

import com.barbershop.entity.Cita;
import com.barbershop.entity.Peluquero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    Optional<Cita> findByCodigoUnico(String codigoUnico);
    
    @Query("SELECT c FROM Cita c WHERE c.peluquero = :peluquero AND c.fechaHora BETWEEN :inicio AND :fin AND c.estado = 'PROGRAMADA'")
    List<Cita> findCitasProgramadasByPeluqueroAndFecha(
        @Param("peluquero") Peluquero peluquero,
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
    
    @Query("SELECT c FROM Cita c WHERE c.peluquero.id = :peluqueroId AND DATE(c.fechaHora) = DATE(:fecha) AND c.estado = 'PROGRAMADA'")
    List<Cita> findCitasProgramadasByPeluqueroAndDia(
        @Param("peluqueroId") Long peluqueroId,
        @Param("fecha") LocalDateTime fecha
    );
}

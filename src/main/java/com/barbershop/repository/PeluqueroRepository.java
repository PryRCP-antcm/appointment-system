package com.barbershop.repository;

import com.barbershop.entity.Peluquero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PeluqueroRepository extends JpaRepository<Peluquero, Long> {
    List<Peluquero> findByActivoTrue();
    List<Peluquero> findByActivoFalse();
}

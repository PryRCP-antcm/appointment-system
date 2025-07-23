package com.barbershop.config;

import com.barbershop.entity.Peluquero;
import com.barbershop.entity.Servicio;
import com.barbershop.repository.PeluqueroRepository;
import com.barbershop.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private PeluqueroRepository peluqueroRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Inicializar servicios si no existen
        if (servicioRepository.count() == 0) {
            servicioRepository.save(new Servicio("Corte de Cabello", "Corte tradicional de cabello", new BigDecimal("25.00"), 30));
            servicioRepository.save(new Servicio("Corte + Barba", "Corte de cabello y arreglo de barba", new BigDecimal("35.00"), 45));
            servicioRepository.save(new Servicio("Afeitado Clásico", "Afeitado tradicional con navaja", new BigDecimal("20.00"), 30));
            servicioRepository.save(new Servicio("Lavado + Corte", "Lavado y corte de cabello", new BigDecimal("30.00"), 45));
        }
        
        // Inicializar peluqueros si no existen
        if (peluqueroRepository.count() == 0) {
            peluqueroRepository.save(new Peluquero("Carlos", "Rodríguez", "Especialista en cortes clásicos"));
            peluqueroRepository.save(new Peluquero("María", "González", "Experta en estilos modernos"));
            peluqueroRepository.save(new Peluquero("José", "Martínez", "Maestro en afeitado tradicional"));
        }
    }
}

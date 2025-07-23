package com.barbershop.controller;

import com.barbershop.entity.Servicio;
import com.barbershop.service.CitaService;
import com.barbershop.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private CitaService citaService;
    
    @Autowired
    private ServicioService servicioService;
    
    @GetMapping("/horarios-disponibles")
    public ResponseEntity<List<String>> obtenerHorariosDisponibles(
            @RequestParam Long peluqueroId,
            @RequestParam String fecha,
            @RequestParam Long servicioId) {
        
        Optional<Servicio> servicio = servicioService.buscarPorId(servicioId);
        if (servicio.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<String> horarios = citaService.obtenerHorariosDisponibles(
            peluqueroId, fecha, servicio.get().getDuracionMinutos()
        );
        
        return ResponseEntity.ok(horarios);
    }
}

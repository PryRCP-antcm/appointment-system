package com.barbershop.service;

import com.barbershop.entity.Cita;
import com.barbershop.entity.Cliente;
import com.barbershop.entity.Peluquero;
import com.barbershop.entity.Servicio;
import com.barbershop.repository.CitaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CitaService {
    
    @Autowired
    private CitaRepository citaRepository;
    
    public Cita guardarCita(Cita cita) {
        return citaRepository.save(cita);
    }
    
    public Optional<Cita> buscarPorCodigoUnico(String codigoUnico) {
        return citaRepository.findByCodigoUnico(codigoUnico);
    }
    
    public String generarCodigoUnico() {
        String codigo;
        do {
            codigo = generarCodigoAleatorio();
        } while (citaRepository.findByCodigoUnico(codigo).isPresent());
        return codigo;
    }
    
    private String generarCodigoAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }
    
    public List<String> obtenerHorariosDisponibles(Long peluqueroId, String fecha, Integer duracionServicio) {
        LocalDateTime fechaSeleccionada = LocalDateTime.parse(fecha + "T00:00:00");
        
        // Horarios de trabajo: 9:00 AM a 6:00 PM
        LocalTime horaInicio = LocalTime.of(9, 0);
        LocalTime horaFin = LocalTime.of(18, 0);
        
        List<Cita> citasDelDia = citaRepository.findCitasProgramadasByPeluqueroAndDia(peluqueroId, fechaSeleccionada);
        
        List<String> horariosDisponibles = new ArrayList<>();
        LocalTime horaActual = horaInicio;
        
        while (horaActual.plusMinutes(duracionServicio).isBefore(horaFin) || 
               horaActual.plusMinutes(duracionServicio).equals(horaFin)) {
            
            LocalDateTime fechaHoraActual = fechaSeleccionada.with(horaActual);
            
            if (esHorarioDisponible(fechaHoraActual, duracionServicio, citasDelDia)) {
                horariosDisponibles.add(horaActual.toString());
            }
            
            horaActual = horaActual.plusMinutes(30); // Intervalos de 30 minutos
        }
        
        return horariosDisponibles;
    }
    
    private boolean esHorarioDisponible(LocalDateTime fechaHora, Integer duracionServicio, List<Cita> citasDelDia) {
        LocalDateTime finNuevaCita = fechaHora.plusMinutes(duracionServicio);
        
        for (Cita cita : citasDelDia) {
            LocalDateTime inicioCitaExistente = cita.getFechaHora();
            LocalDateTime finCitaExistente = inicioCitaExistente.plusMinutes(cita.getServicio().getDuracionMinutos());
            
            // Verificar si hay conflicto de horarios
            if (fechaHora.isBefore(finCitaExistente) && finNuevaCita.isAfter(inicioCitaExistente)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Transactional
    public Cita crearCita(Cliente cliente, Servicio servicio, Peluquero peluquero, LocalDateTime fechaHora) {
        String codigoUnico = generarCodigoUnico();
        Cita cita = new Cita(cliente, servicio, peluquero, fechaHora, codigoUnico);
        return guardarCita(cita);
    }
    
    public List<Cita> listarTodasCitas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> buscarPorId(Long id) {
    return citaRepository.findById(id);
}

public Cita guardar(Cita cita) {
    return citaRepository.save(cita);
}
}

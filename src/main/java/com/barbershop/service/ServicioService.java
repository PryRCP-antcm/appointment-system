package com.barbershop.service;

import com.barbershop.entity.Servicio;
import com.barbershop.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> obtenerServiciosActivos() {
        return servicioRepository.findByActivoTrue();
    }

    public Optional<Servicio> buscarPorId(Long id) {
        return servicioRepository.findById(id);
    }

    public List<Servicio> obtenerTodosServicios() {
        return servicioRepository.findAll();
    }

    public Servicio guardarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public Optional<Servicio> editarServicio(Long id, Servicio servicio) {
        return servicioRepository.findById(id).map(servicioExistente -> {
            servicioExistente.setNombre(servicio.getNombre());
            servicioExistente.setDescripcion(servicio.getDescripcion());
            servicioExistente.setPrecio(servicio.getPrecio());
            servicioExistente.setDuracionMinutos(servicio.getDuracionMinutos());
            servicioExistente.setActivo(servicio.getActivo());

            return servicioRepository.save(servicioExistente);
        });
    }
    public void eliminarServicio(Long id) {
        servicioRepository.deleteById(id);
    }
}
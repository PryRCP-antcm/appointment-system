package com.barbershop.service;

import com.barbershop.entity.Peluquero;
import com.barbershop.repository.PeluqueroRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PeluqueroService {

    @Autowired
    private PeluqueroRepository peluqueroRepository;

    public List<Peluquero> obtenerPeluquerosActivos() {
        return peluqueroRepository.findByActivoTrue();
    }

    public List<Peluquero> obtenerPeluquerosInactivos() {
        return peluqueroRepository.findByActivoFalse();
    }

    public List<Peluquero> obtenerTodosPeluqueros() {
        return peluqueroRepository.findAll();
    }

    public Optional<Peluquero> buscarPorId(Long id) {
        return peluqueroRepository.findById(id);
    }

    public Peluquero guardarPeluquero(Peluquero peluquero) {
        return peluqueroRepository.save(peluquero);
    }

    @Transactional
    public Optional<Peluquero> editarPeluquero(Long id, Peluquero peluquero) {
        return peluqueroRepository.findById(id).map(peluqueroExistente -> {
            peluqueroExistente.setNombre(peluquero.getNombre());
            peluqueroExistente.setApellido(peluquero.getApellido());
            peluqueroExistente.setEspecialidad(peluquero.getEspecialidad());
            peluqueroExistente.setActivo(peluquero.getActivo());

            return peluqueroRepository.save(peluqueroExistente);
        });
    }

    public void eliminarPeluquero(Long id) {
        peluqueroRepository.deleteById(id);
    }

}

package com.barbershop.service;

import com.barbershop.entity.Cliente;
import com.barbershop.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
    
    public boolean existeTelefono(String telefono) {
        return clienteRepository.existsByTelefono(telefono);
    }
    
    public Cliente buscarOCrearCliente(String nombre, String apellido, String telefono, String email) {
        Optional<Cliente> clienteExistente = buscarPorEmail(email);
        if (clienteExistente.isPresent()) {
            return clienteExistente.get();
        }
        
        Cliente nuevoCliente = new Cliente(nombre, apellido, telefono, email);
        return guardarCliente(nuevoCliente);
    }
}

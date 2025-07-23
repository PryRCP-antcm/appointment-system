package com.barbershop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "peluqueros")
public class Peluquero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del peluquero es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido del peluquero es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @Column(length = 500)
    private String especialidad;

    @Column(nullable = false)
    private Boolean activo = true;

    // Constructors
    public Peluquero() {}

    public Peluquero(String nombre, String apellido, String especialidad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}

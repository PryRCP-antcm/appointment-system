package com.barbershop.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.barbershop.entity.Servicio;
import com.barbershop.service.ServicioService;

@Controller
@RequestMapping("/admin/servicios")
public class AdminServiciosController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "admin-servicio-form";
    }
     // Guardar servicio nuevo
    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute Servicio servicio, RedirectAttributes redirectAttributes) {
        servicioService.guardarServicio(servicio);
        redirectAttributes.addFlashAttribute("success", "Servicio creado con éxito");
        return "redirect:/admin/servicios/listar";
    }

    // Mostrar lista de servicios
    @GetMapping("/listar")
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.obtenerTodosServicios());
        return "admin-servicio-list";
    }

    // Mostrar formulario para editar servicio
@PostMapping("/editar")
public String mostrarFormularioEditarPost(@ModelAttribute("id") Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Servicio> servicioOpt = servicioService.buscarPorId(id);
    if (servicioOpt.isPresent()) {
        model.addAttribute("servicio", servicioOpt.get());
        return "admin-servicio-form";
    } else {
        redirectAttributes.addFlashAttribute("error", "Servicio no encontrado");
        return "redirect:/admin/servicios/listar";
    }
}


    // Actualizar servicio
    @PostMapping("/actualizar/{id}")
    public String actualizarPeluquero(@PathVariable Long id, @ModelAttribute Servicio servicio, RedirectAttributes redirectAttributes) {
        Optional<Servicio> servicoActualizado = servicioService.editarServicio(id, servicio);
        if (servicoActualizado.isPresent()) {
            redirectAttributes.addFlashAttribute("success", "Servicio actualizado con éxito");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar, servicio no encontrado");
        }
        return "redirect:/admin/servicios/listar";
    }
}

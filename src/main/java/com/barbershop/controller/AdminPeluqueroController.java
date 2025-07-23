package com.barbershop.controller;

import com.barbershop.entity.Peluquero;
import com.barbershop.service.PeluqueroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/peluqueros")
public class AdminPeluqueroController {

    @Autowired
    private PeluqueroService peluqueroService;

    // Mostrar formulario para crear peluquero
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("peluquero", new Peluquero());
        return "admin-peluquero-form";
    }

    // Guardar peluquero nuevo
    @PostMapping("/guardar")
    public String guardarPeluquero(@ModelAttribute Peluquero peluquero, RedirectAttributes redirectAttributes) {
        peluqueroService.guardarPeluquero(peluquero);
        redirectAttributes.addFlashAttribute("success", "Peluquero creado con éxito");
        return "redirect:/admin/peluqueros/listar";
    }

    // Mostrar lista de peluqueros
    @GetMapping("/listar")
    public String listarPeluqueros(Model model) {
        model.addAttribute("peluqueros", peluqueroService.obtenerTodosPeluqueros());
        return "admin-peluquero-list";
    }

    // Mostrar formulario para editar peluquero
// Antes (con ID en URL): GET /editar/5
// Ahora (sin ID en la URL): POST /editar

@PostMapping("/editar")
public String mostrarFormularioEditarPost(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Peluquero> peluqueroOpt = peluqueroService.buscarPorId(id);
    if (peluqueroOpt.isPresent()) {
        model.addAttribute("peluquero", peluqueroOpt.get());
        return "admin-peluquero-form";
    } else {
        redirectAttributes.addFlashAttribute("error", "Peluquero no encontrado");
        return "redirect:/admin/peluqueros/listar";
    }
}


    // Actualizar peluquero
    @PostMapping("/actualizar/{id}")
    public String actualizarPeluquero(@PathVariable Long id, @ModelAttribute Peluquero peluquero,
            RedirectAttributes redirectAttributes) {
        Optional<Peluquero> peluqueroActualizado = peluqueroService.editarPeluquero(id, peluquero);
        if (peluqueroActualizado.isPresent()) {
            redirectAttributes.addFlashAttribute("success", "Peluquero actualizado con éxito");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar, peluquero no encontrado");
        }
        return "redirect:/admin/peluqueros/listar";
    }

    // Eliminar peluquero
    @PostMapping("/eliminar/{id}")
    public String eliminarPeluquero(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            peluqueroService.eliminarPeluquero(id);
            redirectAttributes.addFlashAttribute("success", "Peluquero eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el peluquero");
        }
        return "redirect:/admin/peluqueros/listar";
    }

}

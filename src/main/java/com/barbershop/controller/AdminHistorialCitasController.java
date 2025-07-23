package com.barbershop.controller;

import com.barbershop.entity.Cita;
import com.barbershop.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/citas")
public class AdminHistorialCitasController {

    @Autowired
    private CitaService citaService;

    // Mostrar historial de citas
    @GetMapping("/listar")
    public String listarCitas(Model model) {
        List<Cita> citas = citaService.listarTodasCitas(); // o listarTodasLasCitas()
        model.addAttribute("citas", citas);
        return "admin-citas-list"; // Debe existir un archivo admin-citas-list.html en templates
    }

    
    @PostMapping("/actualizarEstado")
    public String actualizarEstado(@RequestParam Long id, @RequestParam String estado, RedirectAttributes redirectAttrs) {
        Optional<Cita> citaOpt = citaService.buscarPorId(id);
        if (citaOpt.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Cita no encontrada");
            return "redirect:/admin/citas/listar";
        }

        try {
            Cita cita = citaOpt.get();
            cita.setEstado(Cita.EstadoCita.valueOf(estado));
            citaService.guardar(cita);
            redirectAttrs.addFlashAttribute("success", "Estado actualizado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("error", "Estado inválido");
        }

        return "redirect:/admin/citas/listar";
    }
}

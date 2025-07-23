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
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private CitaService citaService;
    
    @GetMapping("/login")
    public String mostrarLogin() {
        return "admin-login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin-dashboard";
    }
    
    
    @PostMapping("/verificar-cita")
    public String verificarCita(@RequestParam String codigoUnico, 
                              Model model, 
                              RedirectAttributes redirectAttributes) {
        if (codigoUnico == null || codigoUnico.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debe ingresar un código");
            return "redirect:/admin/dashboard";
        }
        
        Optional<Cita> cita = citaService.buscarPorCodigoUnico(codigoUnico.trim().toUpperCase());
        
        if (cita.isPresent()) {
            model.addAttribute("cita", cita.get());
            model.addAttribute("encontrada", true);
        } else {
            model.addAttribute("encontrada", false);
            model.addAttribute("codigoBuscado", codigoUnico);
        }
        
        return "admin-dashboard";
    }
}

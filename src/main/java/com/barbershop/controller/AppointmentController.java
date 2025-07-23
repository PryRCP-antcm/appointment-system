package com.barbershop.controller;

import com.barbershop.entity.Cliente;
import com.barbershop.entity.Cita;
import com.barbershop.entity.Peluquero;
import com.barbershop.entity.Servicio;
import com.barbershop.service.CitaService;
import com.barbershop.service.ClienteService;
import com.barbershop.service.PeluqueroService;
import com.barbershop.service.ServicioService;
import com.barbershop.service.TwilioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class AppointmentController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private PeluqueroService peluqueroService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private TwilioService twilioService;

    @GetMapping
    public String inicio() {
        return "index";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("servicios", servicioService.obtenerServiciosActivos());
        return "servicios";
    }

    @GetMapping("/agendar")
    public String mostrarFormularioCliente(Model model, HttpSession session) {
        // Limpiar sesión al iniciar nuevo proceso
        session.removeAttribute("clienteData");
        session.removeAttribute("servicioId");
        session.removeAttribute("peluqueroId");
        session.removeAttribute("fechaHora");

        model.addAttribute("cliente", new Cliente());
        return "cliente-form";
    }

    @PostMapping("/agendar/cliente")
    public String procesarCliente(@Valid @ModelAttribute Cliente cliente,
            BindingResult result,
            HttpSession session,
            Model model) {
        if (result.hasErrors()) {
            return "cliente-form";
        }

        // Guardar datos del cliente en sesión
        session.setAttribute("clienteData", cliente);

        // Cargar servicios para el siguiente paso
        model.addAttribute("servicios", servicioService.obtenerServiciosActivos());
        return "servicio-form";
    }

    @PostMapping("/agendar/servicio")
    public String procesarServicio(@RequestParam Long servicioId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Servicio> servicio = servicioService.buscarPorId(servicioId);
        if (servicio.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Servicio no encontrado");
            return "redirect:/agendar";
        }

        session.setAttribute("servicioId", servicioId);

        // Cargar peluqueros para el siguiente paso
        model.addAttribute("peluqueros", peluqueroService.obtenerPeluquerosActivos());
        model.addAttribute("servicioSeleccionado", servicio.get());
        return "peluquero-form";
    }

    @PostMapping("/agendar/peluquero")
    public String procesarPeluquero(@RequestParam Long peluqueroId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Peluquero> peluquero = peluqueroService.buscarPorId(peluqueroId);
        if (peluquero.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Peluquero no encontrado");
            return "redirect:/agendar";
        }

        session.setAttribute("peluqueroId", peluqueroId);

        // Preparar datos para selección de fecha y hora
        model.addAttribute("peluqueroSeleccionado", peluquero.get());

        Long servicioId = (Long) session.getAttribute("servicioId");
        Optional<Servicio> servicio = servicioService.buscarPorId(servicioId);
        model.addAttribute("servicioSeleccionado", servicio.get());

        return "fecha-hora-form";
    }

    @PostMapping("/agendar/confirmar")
    public String confirmarCita(@RequestParam String fecha,
            @RequestParam String hora,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Recuperar datos de la sesión
            Cliente clienteData = (Cliente) session.getAttribute("clienteData");
            Long servicioId = (Long) session.getAttribute("servicioId");
            Long peluqueroId = (Long) session.getAttribute("peluqueroId");

            if (clienteData == null || servicioId == null || peluqueroId == null) {
                redirectAttributes.addFlashAttribute("error", "Datos de sesión incompletos");
                return "redirect:/agendar";
            }

            // Buscar o crear cliente
            Cliente cliente = clienteService.buscarOCrearCliente(
                    clienteData.getNombre(),
                    clienteData.getApellido(),
                    clienteData.getTelefono(),
                    clienteData.getEmail());

            // Obtener servicio y peluquero
            Optional<Servicio> servicio = servicioService.buscarPorId(servicioId);
            Optional<Peluquero> peluquero = peluqueroService.buscarPorId(peluqueroId);

            if (servicio.isEmpty() || peluquero.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Datos no válidos");
                return "redirect:/agendar";
            }

            // Crear fecha y hora
            LocalDateTime fechaHora = LocalDateTime.parse(fecha + "T" + hora + ":00");

            // Crear la cita
            Cita cita = citaService.crearCita(cliente, servicio.get(), peluquero.get(), fechaHora);

            String telefono = cliente.getTelefono();
            if (!telefono.startsWith("+")) {
                telefono = "+51" + telefono; // Asegura el formato internacional
            }

            String mensajeSms = String.format(
                    "Hola %s, tu cita (código %s) ha sido agendada para el %s con el servicio %s. ¡Te esperamos!",
                    cliente.getNombre(), cita.getCodigoUnico(), fechaHora.toString(), servicio.get().getNombre());

            // Enviar SMS
            twilioService.enviarSms(telefono, mensajeSms);

            // Limpiar sesión
            session.invalidate();

            // Mostrar confirmación
            model.addAttribute("cita", cita);
            return "confirmacion";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al crear la cita: " + e.getMessage());
            return "redirect:/agendar";
        }
    }
}

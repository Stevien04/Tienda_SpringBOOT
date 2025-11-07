package com.tienda.controlador;

import com.tienda.modelo.modeloCargo;
import com.tienda.servicio.servicioCargo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cargo")
public class controladorCargo {

    private final servicioCargo servicioCargo;

    public controladorCargo(servicioCargo servicioCargo) {
        this.servicioCargo = servicioCargo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("activos", servicioCargo.listarActivos());
        model.addAttribute("inactivos", servicioCargo.listarInactivos());
        return "Cargo/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        if (!model.containsAttribute("cargo")) {
            modeloCargo nuevoCargo = new modeloCargo();
            nuevoCargo.activar();
            model.addAttribute("cargo", nuevoCargo);
        }
        return "Cargo/nuevo";
    }

    @PostMapping("/nuevo")
    public String crear(@ModelAttribute("cargo") modeloCargo cargo, RedirectAttributes redirectAttributes) {
        String nombreNormalizado = servicioCargo.normalizarNombre(cargo.getNombre());
        cargo.setNombre(nombreNormalizado);

        if (nombreNormalizado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre del cargo es obligatorio.");
            redirectAttributes.addFlashAttribute("cargo", cargo);
            return "redirect:/cargo/nuevo";
        }

        if (servicioCargo.existeNombre(nombreNormalizado)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un cargo con el nombre proporcionado.");
            redirectAttributes.addFlashAttribute("cargo", cargo);
            return "redirect:/cargo/nuevo";
        }

        cargo.activar();
        servicioCargo.guardar(cargo);
        redirectAttributes.addFlashAttribute("exito", "Cargo registrado correctamente.");
        return "redirect:/cargo";
    }

    @GetMapping("/{idCargo}/editar")
    public String mostrarFormularioEdicion(@PathVariable Integer idCargo, Model model, RedirectAttributes redirectAttributes) {
        if (model.containsAttribute("cargo")) {
            return "Cargo/editar";
        }

        return servicioCargo.buscarPorId(idCargo)
                .map(cargo -> {
                    model.addAttribute("cargo", cargo);
                    return "Cargo/editar";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El cargo solicitado no existe.");
                    return "redirect:/cargo";
                });
    }

    @PostMapping("/{idCargo}/editar")
    public String actualizar(@PathVariable Integer idCargo,
                             @ModelAttribute("cargo") modeloCargo formulario,
                             RedirectAttributes redirectAttributes) {
        String nombreNormalizado = servicioCargo.normalizarNombre(formulario.getNombre());
        formulario.setIdCargo(idCargo);
        formulario.setNombre(nombreNormalizado);

        if (nombreNormalizado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre del cargo es obligatorio.");
            redirectAttributes.addFlashAttribute("cargo", formulario);
            return "redirect:/cargo/{idCargo}/editar";
        }

        if (servicioCargo.existeNombreParaOtroCargo(nombreNormalizado, idCargo)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un cargo con el nombre proporcionado.");
            redirectAttributes.addFlashAttribute("cargo", formulario);
            return "redirect:/cargo/{idCargo}/editar";
        }

        return servicioCargo.actualizarNombre(idCargo, nombreNormalizado)
                .map(actualizado -> {
                    redirectAttributes.addFlashAttribute("exito", "Cargo actualizado correctamente.");
                    return "redirect:/cargo";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "El cargo solicitado no existe.");
                    return "redirect:/cargo";
                });
    }

    @PostMapping("/{idCargo}/estado")
    public String cambiarEstado(@PathVariable Integer idCargo, RedirectAttributes redirectAttributes) {
        if (servicioCargo.cambiarEstado(idCargo)) {
            redirectAttributes.addFlashAttribute("exito", "El estado del cargo se actualizó correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cargo solicitado.");
        }
        return "redirect:/cargo";
    }
}
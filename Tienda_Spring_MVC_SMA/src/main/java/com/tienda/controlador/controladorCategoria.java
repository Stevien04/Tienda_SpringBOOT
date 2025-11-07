package com.tienda.controlador;

import com.tienda.modelo.modeloCategoria;
import com.tienda.servicio.servicioCategoria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categoria")
public class controladorCategoria {

    private final servicioCategoria servicioCategoria;

    public controladorCategoria(servicioCategoria servicioCategoria) {
        this.servicioCategoria = servicioCategoria;
    }

    @GetMapping
    public String listar(@RequestParam(value = "q", required = false) String terminoBusqueda, Model model) {
        String filtro = terminoBusqueda == null ? "" : terminoBusqueda.trim();
        model.addAttribute("busqueda", filtro);
        model.addAttribute("activas", servicioCategoria.buscarPorEstadoYNombre(1, filtro));
        model.addAttribute("inactivas", servicioCategoria.buscarPorEstadoYNombre(0, filtro));
        return "Categoria/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        if (!model.containsAttribute("categoria")) {
            modeloCategoria nuevaCategoria = new modeloCategoria();
            nuevaCategoria.activar();
            model.addAttribute("categoria", nuevaCategoria);
        }
        return "Categoria/nuevo";
    }

    @PostMapping("/nuevo")
    public String crear(@ModelAttribute("categoria") modeloCategoria categoria, RedirectAttributes redirectAttributes) {
        String nombreNormalizado = servicioCategoria.normalizarNombre(categoria.getNombre());
        categoria.setNombre(nombreNormalizado);

        if (nombreNormalizado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre de la categoría es obligatorio.");
            redirectAttributes.addFlashAttribute("categoria", categoria);
            return "redirect:/categoria/nuevo";
        }

        if (!servicioCategoria.esNombreValido(nombreNormalizado)) {
            redirectAttributes.addFlashAttribute("error", "El nombre de la categoría solo puede contener letras y espacios.");
            redirectAttributes.addFlashAttribute("categoria", categoria);
            return "redirect:/categoria/nuevo";
        }

        if (servicioCategoria.existeNombre(nombreNormalizado)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe una categoría con el nombre proporcionado.");
            redirectAttributes.addFlashAttribute("categoria", categoria);
            return "redirect:/categoria/nuevo";
        }

        categoria.activar();
        servicioCategoria.guardar(categoria);
        redirectAttributes.addFlashAttribute("exito", "Categoría registrada correctamente.");
        return "redirect:/categoria";
    }

    @GetMapping("/{idCategoria}/editar")
    public String mostrarFormularioEdicion(@PathVariable Integer idCategoria, Model model, RedirectAttributes redirectAttributes) {
        if (model.containsAttribute("categoria")) {
            return "Categoria/editar";
        }

        return servicioCategoria.buscarPorId(idCategoria)
                .map(categoria -> {
                    model.addAttribute("categoria", categoria);
                    return "Categoria/editar";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La categoría solicitada no existe.");
                    return "redirect:/categoria";
                });
    }

    @PostMapping("/{idCategoria}/editar")
    public String actualizar(@PathVariable Integer idCategoria,
                             @ModelAttribute("categoria") modeloCategoria formulario,
                             RedirectAttributes redirectAttributes) {
        String nombreNormalizado = servicioCategoria.normalizarNombre(formulario.getNombre());
        formulario.setIdCategoria(idCategoria);
        formulario.setNombre(nombreNormalizado);

        if (nombreNormalizado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El nombre de la categoría es obligatorio.");
            redirectAttributes.addFlashAttribute("categoria", formulario);
            return "redirect:/categoria/{idCategoria}/editar";
        }

        if (!servicioCategoria.esNombreValido(nombreNormalizado)) {
            redirectAttributes.addFlashAttribute("error", "El nombre de la categoría solo puede contener letras y espacios.");
            redirectAttributes.addFlashAttribute("categoria", formulario);
            return "redirect:/categoria/{idCategoria}/editar";
        }

        if (servicioCategoria.existeNombreParaOtraCategoria(nombreNormalizado, idCategoria)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe una categoría con el nombre proporcionado.");
            redirectAttributes.addFlashAttribute("categoria", formulario);
            return "redirect:/categoria/{idCategoria}/editar";
        }

        return servicioCategoria.actualizarNombre(idCategoria, nombreNormalizado)
                .map(actualizada -> {
                    redirectAttributes.addFlashAttribute("exito", "Categoría actualizada correctamente.");
                    return "redirect:/categoria";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "La categoría solicitada no existe.");
                    return "redirect:/categoria";
                });
    }

    @PostMapping("/{idCategoria}/estado")
    public String cambiarEstado(@PathVariable Integer idCategoria, RedirectAttributes redirectAttributes) {
        if (servicioCategoria.cambiarEstado(idCategoria)) {
            redirectAttributes.addFlashAttribute("exito", "El estado de la categoría se actualizó correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la categoría solicitada.");
        }
        return "redirect:/categoria";
    }
}
package com.tienda.servicio;

import com.tienda.modelo.modeloCategoria;
import com.tienda.repositorio.repositorioCategoria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class servicioCategoria {

    private static final Pattern NOMBRE_VALIDO = Pattern.compile("^[A-ZÁÉÍÓÚÜÑ\\s]+$");

    private final repositorioCategoria repositorioCategoria;

    public servicioCategoria(repositorioCategoria repositorioCategoria) {
        this.repositorioCategoria = repositorioCategoria;
    }

    @Transactional(readOnly = true)
    public List<modeloCategoria> listarActivas() {
        return repositorioCategoria.findByEstadoOrderByNombreAsc(1);
    }

    @Transactional(readOnly = true)
    public List<modeloCategoria> listarInactivas() {
        return repositorioCategoria.findByEstadoOrderByNombreAsc(0);
    }

    @Transactional(readOnly = true)
    public List<modeloCategoria> buscarPorEstadoYNombre(Integer estado, String termino) {
        String filtro = termino == null ? "" : termino.trim();
        if (filtro.isEmpty()) {
            return estado == 1 ? listarActivas() : listarInactivas();
        }
        return repositorioCategoria.findByEstadoAndNombreContainingIgnoreCaseOrderByNombreAsc(estado, filtro);
    }

    @Transactional(readOnly = true)
    public Optional<modeloCategoria> buscarPorId(Integer idCategoria) {
        return repositorioCategoria.findById(idCategoria);
    }

    @Transactional
    public modeloCategoria guardar(modeloCategoria categoria) {
        return repositorioCategoria.save(categoria);
    }

    @Transactional(readOnly = true)
    public boolean existeNombre(String nombreNormalizado) {
        return repositorioCategoria.existsByNombreIgnoreCase(nombreNormalizado);
    }

    @Transactional(readOnly = true)
    public boolean existeNombreParaOtraCategoria(String nombreNormalizado, Integer idCategoria) {
        return repositorioCategoria.existsByNombreIgnoreCaseAndIdCategoriaNot(nombreNormalizado, idCategoria);
    }

    @Transactional
    public Optional<modeloCategoria> actualizarNombre(Integer idCategoria, String nombreNormalizado) {
        return repositorioCategoria.findById(idCategoria).map(categoriaExistente -> {
            categoriaExistente.setNombre(nombreNormalizado);
            return repositorioCategoria.save(categoriaExistente);
        });
    }

    @Transactional
    public boolean cambiarEstado(Integer idCategoria) {
        Optional<modeloCategoria> categoriaOptional = repositorioCategoria.findById(idCategoria);
        if (categoriaOptional.isEmpty()) {
            return false;
        }

        modeloCategoria categoria = categoriaOptional.get();
        categoria.alternarEstado();
        repositorioCategoria.save(categoria);
        return true;
    }

    public String normalizarNombre(String nombre) {
        return nombre == null ? "" : nombre.trim().toUpperCase();
    }

    public boolean esNombreValido(String nombreNormalizado) {
        return NOMBRE_VALIDO.matcher(nombreNormalizado).matches();
    }
}
package com.tienda.servicio;

import com.tienda.modelo.modeloCargo;
import com.tienda.repositorio.repositorioCargo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class servicioCargo {
    
    private static final Pattern NOMBRE_VALIDO = Pattern.compile("^[A-ZÁÉÍÓÚÜÑ\\s]+$");
    
    private final repositorioCargo repositorioCargo;

    public servicioCargo(repositorioCargo repositorioCargo) {
        this.repositorioCargo = repositorioCargo;
    }

    @Transactional(readOnly = true)
    public List<modeloCargo> listarActivos() {
        return repositorioCargo.findByEstadoOrderByNombreAsc(1);
    }

    @Transactional(readOnly = true)
    public List<modeloCargo> listarInactivos() {
        return repositorioCargo.findByEstadoOrderByNombreAsc(0);
    }
    
     @Transactional(readOnly = true)
    public List<modeloCargo> buscarPorEstadoYNombre(Integer estado, String termino) {
        String filtro = termino == null ? "" : termino.trim();
        if (filtro.isEmpty()) {
            return estado == 1 ? listarActivos() : listarInactivos();
        }
        return repositorioCargo.findByEstadoAndNombreContainingIgnoreCaseOrderByNombreAsc(estado, filtro);
    }

    @Transactional(readOnly = true)
    public Optional<modeloCargo> buscarPorId(Integer idCargo) {
        return repositorioCargo.findById(idCargo);
    }

    @Transactional
    public modeloCargo guardar(modeloCargo cargo) {
        return repositorioCargo.save(cargo);
    }

    @Transactional(readOnly = true)
    public boolean existeNombre(String nombreNormalizado) {
        return repositorioCargo.existsByNombreIgnoreCase(nombreNormalizado);
    }

    @Transactional(readOnly = true)
    public boolean existeNombreParaOtroCargo(String nombreNormalizado, Integer idCargo) {
        return repositorioCargo.existsByNombreIgnoreCaseAndIdCargoNot(nombreNormalizado, idCargo);
    }

    @Transactional
    public Optional<modeloCargo> actualizarNombre(Integer idCargo, String nombreNormalizado) {
        return repositorioCargo.findById(idCargo).map(cargoExistente -> {
            cargoExistente.setNombre(nombreNormalizado);
            return repositorioCargo.save(cargoExistente);
        });
    }

    @Transactional
    public boolean cambiarEstado(Integer idCargo) {
        Optional<modeloCargo> cargoOptional = repositorioCargo.findById(idCargo);
        if (cargoOptional.isEmpty()) {
            return false;
        }

        modeloCargo cargo = cargoOptional.get();
        cargo.alternarEstado();
        repositorioCargo.save(cargo);
        return true;
    }

    public String normalizarNombre(String nombre) {
        return nombre == null ? "" : nombre.trim().toUpperCase();
    }
    
     public boolean esNombreValido(String nombreNormalizado) {
        return NOMBRE_VALIDO.matcher(nombreNormalizado).matches();
    }
}
package com.tienda.repositorio;

import com.tienda.modelo.modeloCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface repositorioCargo extends JpaRepository<modeloCargo, Integer> {

    List<modeloCargo> findByEstadoOrderByNombreAsc(Integer estado);
    
    List<modeloCargo> findByEstadoAndNombreContainingIgnoreCaseOrderByNombreAsc(Integer estado, String nombre);
    
    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCargoNot(String nombre, Integer idCargo);
    
    
}
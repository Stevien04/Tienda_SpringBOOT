package com.tienda.repositorio;

import com.tienda.modelo.modeloCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface repositorioCategoria extends JpaRepository<modeloCategoria, Integer> {

    List<modeloCategoria> findByEstadoOrderByNombreAsc(Integer estado);

    List<modeloCategoria> findByEstadoAndNombreContainingIgnoreCaseOrderByNombreAsc(Integer estado, String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, Integer idCategoria);
}
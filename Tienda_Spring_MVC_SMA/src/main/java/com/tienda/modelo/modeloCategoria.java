package com.tienda.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tbcategoria")
public class modeloCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategoria")
    private Integer idCategoria;

    @Column(name = "Categoria", nullable = false, length = 20)
    private String nombre;

    @Column(name = "Estado", nullable = false)
    private Integer estado = 1;

    public modeloCategoria() {
    }

    public modeloCategoria(String nombre, Integer estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public modeloCategoria(Integer idCategoria, String nombre, Integer estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public boolean isActiva() {
        return Integer.valueOf(1).equals(this.estado);
    }

    public void activar() {
        this.estado = 1;
    }

    public void desactivar() {
        this.estado = 0;
    }

    public void alternarEstado() {
        this.estado = isActiva() ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof modeloCategoria categoria)) {
            return false;
        }
        return Objects.equals(idCategoria, categoria.idCategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria);
    }

    @Override
    public String toString() {
        return "modeloCategoria{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}
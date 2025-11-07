package com.tienda.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tbcargo")
public class modeloCargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcargo")
    private Integer idCargo;

    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;

    @Column(name = "estado", nullable = false)
    private Integer estado = 1;

    public modeloCargo() {
    }

    public modeloCargo(String nombre, Integer estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public modeloCargo(Integer idCargo, String nombre, Integer estado) {
        this.idCargo = idCargo;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
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

    public boolean isActivo() {
        return Integer.valueOf(1).equals(this.estado);
    }

    public void activar() {
        this.estado = 1;
    }

    public void desactivar() {
        this.estado = 0;
    }

    public void alternarEstado() {
        this.estado = isActivo() ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof modeloCargo cargo)) {
            return false;
        }
        return Objects.equals(idCargo, cargo.idCargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCargo);
    }

    @Override
    public String toString() {
        return "modeloCargo{" +
                "idCargo=" + idCargo +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}
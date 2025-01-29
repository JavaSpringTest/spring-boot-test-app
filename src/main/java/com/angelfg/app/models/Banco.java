package com.angelfg.app.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "bancos")
public class Banco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    @Column(name = "total_transferencias")
    private int totalTransferencias;

    public Banco() {
    }

    public Banco(Long id, String nombre, int totalTransferencias) {
        this.id = id;
        this.nombre = nombre;
        this.totalTransferencias = totalTransferencias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTotalTransferencias() {
        return totalTransferencias;
    }

    public void setTotalTransferencias(int totalTransferencias) {
        this.totalTransferencias = totalTransferencias;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Banco banco1 = (Banco) o;
        return totalTransferencias == banco1.totalTransferencias && Objects.equals(id, banco1.id) && Objects.equals(nombre, banco1.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, totalTransferencias);
    }

}

package com.angelfg.app.models;

import java.util.Objects;

public class Banco {

    private Long id;
    private String banco;
    private int totalTransferencias;

    public Banco() {
    }

    public Banco(Long id, String banco, int totalTransferencias) {
        this.id = id;
        this.banco = banco;
        this.totalTransferencias = totalTransferencias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
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
        return totalTransferencias == banco1.totalTransferencias && Objects.equals(id, banco1.id) && Objects.equals(banco, banco1.banco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, banco, totalTransferencias);
    }

}

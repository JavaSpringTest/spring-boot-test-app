package com.angelfg.app.repositories;

import com.angelfg.app.models.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    void update(Cuenta cuenta);
}

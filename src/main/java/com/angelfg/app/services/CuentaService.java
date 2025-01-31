package com.angelfg.app.services;

import com.angelfg.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);
    void deleteById(Long id);
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(
        Long numeroCuentaOrigen,
        Long numeroCuentaDestino,
        BigDecimal monto,
        Long bancoId
    );
}

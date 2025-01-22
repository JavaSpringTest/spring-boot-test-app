package com.angelfg.app.services;

import com.angelfg.app.models.Banco;
import com.angelfg.app.models.Cuenta;
import com.angelfg.app.repositories.BancoRepository;
import com.angelfg.app.repositories.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final BancoRepository bancoRepository;

    public CuentaServiceImpl(
        CuentaRepository cuentaRepository,
        BancoRepository bancoRepository
    ) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return this.cuentaRepository.findById(id);
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco = this.bancoRepository.findById(bancoId);
        return banco.getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = this.cuentaRepository.findById(cuentaId);
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(
        Long numeroCuentaOrigen,
        Long numeroCuentaDestino,
        BigDecimal monto,
        Long bancoId
    ) {
        Cuenta cuentaOrigen = this.cuentaRepository.findById(numeroCuentaOrigen);
        cuentaOrigen.debito(monto);
        this.cuentaRepository.update(cuentaOrigen);

        Cuenta cuentaDestino = this.cuentaRepository.findById(numeroCuentaDestino);
        cuentaDestino.credito(monto);
        this.cuentaRepository.update(cuentaOrigen);

        Banco banco = this.bancoRepository.findById(bancoId);
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        this.bancoRepository.update(banco);
    }

}

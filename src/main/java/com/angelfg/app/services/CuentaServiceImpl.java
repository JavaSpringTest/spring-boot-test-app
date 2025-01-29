package com.angelfg.app.services;

import com.angelfg.app.models.Banco;
import com.angelfg.app.models.Cuenta;
import com.angelfg.app.repositories.BancoRepository;
import com.angelfg.app.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return this.cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco = this.bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = this.cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void transferir(
        Long numeroCuentaOrigen,
        Long numeroCuentaDestino,
        BigDecimal monto,
        Long bancoId
    ) {
        Cuenta cuentaOrigen = this.cuentaRepository.findById(numeroCuentaOrigen).orElseThrow();
        cuentaOrigen.debito(monto);
        this.cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino = this.cuentaRepository.findById(numeroCuentaDestino).orElseThrow();
        cuentaDestino.credito(monto);
        this.cuentaRepository.save(cuentaDestino);

        Banco banco = this.bancoRepository.findById(bancoId).orElseThrow();
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        this.bancoRepository.save(banco);
    }

}

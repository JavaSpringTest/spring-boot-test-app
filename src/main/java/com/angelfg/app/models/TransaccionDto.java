package com.angelfg.app.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransaccionDto implements Serializable {

    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private BigDecimal monto;
    private Long bancoId;

    public TransaccionDto() {
    }

    public Long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(Long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public Long getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(Long cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    @Override
    public String toString() {
        return "TransaccionDto{" +
                "cuentaOrigenId=" + cuentaOrigenId +
                ", cuentaDestinoId=" + cuentaDestinoId +
                ", monto=" + monto +
                ", bancoId=" + bancoId +
                '}';
    }

}

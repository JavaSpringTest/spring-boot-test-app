package com.angelfg.app.controllers;

import com.angelfg.app.models.Cuenta;
import com.angelfg.app.models.TransaccionDto;
import com.angelfg.app.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> listar() {
        return this.cuentaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable(name = "id") Long id) {
        return this.cuentaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta) {
        return this.cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto) {

        this.cuentaService.transferir(
            dto.getCuentaOrigenId(),
            dto.getCuentaDestinoId(),
            dto.getMonto(),
            dto.getBancoId()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        return ResponseEntity.ok(response);
    }

}

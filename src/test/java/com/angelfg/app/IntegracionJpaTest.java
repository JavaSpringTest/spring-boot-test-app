package com.angelfg.app;

import com.angelfg.app.models.Cuenta;
import com.angelfg.app.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Habilita los test para el repository
public class IntegracionJpaTest {
    
    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = this.cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Luis", cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = this.cuentaRepository.findByPersona("Luis");
        assertTrue(cuenta.isPresent());
        assertEquals("Luis", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = this.cuentaRepository.findByPersona("Rod");

//        assertThrows(NoSuchElementException.class, () -> {
//            cuenta.orElseThrow();
//        });

//        assertThrows(NoSuchElementException.class, () -> cuenta.orElseThrow());

        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = this.cuentaRepository.findAll();

        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {

        // Given
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        // When
        Cuenta cuenta = this.cuentaRepository.save(cuentaPepe);

        // Cuenta cuenta = this.cuentaRepository.findByPersona("Pepe").orElseThrow();
        // Cuenta cuenta = this.cuentaRepository.findById(cuenta.getId()).orElseThrow();

        // Then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        // assertEquals(3, cuenta.getId());

    }

}

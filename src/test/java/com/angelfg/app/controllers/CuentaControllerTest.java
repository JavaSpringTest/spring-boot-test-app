package com.angelfg.app.controllers;

import com.angelfg.app.models.Cuenta;
import com.angelfg.app.models.TransaccionDto;
import com.angelfg.app.services.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.angelfg.app.Datos.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc; // contexto para probar web

    @MockitoBean
    private CuentaService cuentaService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testDetalle() throws Exception {

        // SIMULACION DE NUESTRO ENDPOINT

        // Given
        when(cuentaService.findById(1L)).thenReturn(crearCuenta001().orElseThrow());

        // When
        mvc.perform(MockMvcRequestBuilders
            .get("/api/cuentas/1")
            .contentType(MediaType.APPLICATION_JSON)
        ) // Then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.persona").value("Luis"))
        .andExpect(jsonPath("$.saldo").value("1000"));

        verify(cuentaService).findById(1L); // Verifica que llamo al servicio
    }

    @Test
    void testTransferir() throws Exception {

        // Todo en mvc es autentico lo demas es simulado

        // Given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        System.out.println(this.objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        System.out.println(this.objectMapper.writeValueAsString(response));

        // When
        mvc.perform(post("/api/cuentas/transferir")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(dto))
        )// Then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
        .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con éxito"))
        .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId()))
        .andExpect(content().json(this.objectMapper.writeValueAsString(response)));

    }

    @Test
    void testListar() throws Exception {
        // Given
        List<Cuenta> cuentas = Arrays.asList(
            crearCuenta001().orElseThrow(), crearCuenta002().orElseThrow()
        );

        when(this.cuentaService.findAll()).thenReturn(cuentas);

        // When
        mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
            // Then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].persona").value("Luis"))
            .andExpect(jsonPath("$[1].persona").value("Andres"))
            .andExpect(jsonPath("$[0].saldo").value("1000"))
            .andExpect(jsonPath("$[1].saldo").value("2000"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(content().json(this.objectMapper.writeValueAsString(cuentas)));

        verify(cuentaService).findAll();
    }

    @Test
    void testGuardar() throws Exception {
        // Given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        //when(this.cuentaService.save(any())).thenReturn(cuenta);
        when(this.cuentaService.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // When
        mvc.perform(post("/api/cuentas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(cuenta))
        ) // Then
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(3)))
        .andExpect(jsonPath("$.persona", is("Pepe")))
        .andExpect(jsonPath("$.saldo", is(3000)));

        verify(cuentaService).save(any());
    }

}
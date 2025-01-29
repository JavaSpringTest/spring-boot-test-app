package com.angelfg.app.controllers;

import com.angelfg.app.services.CuentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.angelfg.app.Datos.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc; // contexto para probar web

    @MockitoBean
    private CuentaService cuentaService;

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

}
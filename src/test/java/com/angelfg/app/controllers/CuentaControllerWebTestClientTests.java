package com.angelfg.app.controllers;

import com.angelfg.app.models.Cuenta;
import com.angelfg.app.models.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

// PRUEBAS DE INTEGRACION
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Para poner ordenes en los test (Cual se ejecuta primero, etc)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerWebTestClientTests {

    @Autowired
    private WebTestClient client;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {

        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setBancoId(1L);
        dto.setMonto(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        // When
//        client.post().uri("http://localhost:3030/api/cuentas/transferir")
        client.post().uri("/api/cuentas/transferir") // Cuando es el mismp proyecto no es necesario indicar la ruta completa
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                // Then
                .expectStatus().isOk()
                //.expectBody(String.class) // Puedo obtenerlo como un string y luego convertirlo
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .consumeWith(respuesta -> {
                        try {
//                            Caundo es string en .expectBody(String.class)
//                            String jsonStr = respuesta.getResponseBody();
//                            JsonNode json = this.objectMapper.readTree(jsonStr);

                            JsonNode json = this.objectMapper.readTree(respuesta.getResponseBody());

                            assertEquals("Transferencia realizada con éxito", json.path("mensaje").asText());
                            assertEquals(1L, json.path("transaccion").path("cuentaOrigenId").asLong());
                            assertEquals(LocalDate.now().toString(), json.path("date").asText());
                            assertEquals("100", json.path("transaccion").path("monto").asText());

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .jsonPath("$.mensaje").isNotEmpty()
                    .jsonPath("$.mensaje").value(is("Transferencia realizada con éxito"))
                    .jsonPath("$.mensaje").value(valor -> assertEquals("Transferencia realizada con éxito", valor))
                    .jsonPath("$.mensaje").isEqualTo("Transferencia realizada con éxito")
                    .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
                    .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(this.objectMapper.writeValueAsString(response));

    }

    @Test
    @Order(2)
    void testDetalle() throws JsonProcessingException {

        Cuenta cuenta = new Cuenta(1L, "Luis", new BigDecimal("900"));

        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Luis")
                .jsonPath("$.saldo").isEqualTo(900)
                .json(this.objectMapper.writeValueAsString(cuenta));

    }

    @Test
    @Order(3)
    void testDetalle2() {

        // OTRA FORMA DE OBTENER LA RESPUESTA Y PROBARLA
        client.get().uri("/api/cuentas/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                   Cuenta cuenta = response.getResponseBody();

                   assertEquals("Andres", cuenta.getPersona());
                   assertEquals("2100.00", cuenta.getSaldo().toPlainString());

                });

    }

}
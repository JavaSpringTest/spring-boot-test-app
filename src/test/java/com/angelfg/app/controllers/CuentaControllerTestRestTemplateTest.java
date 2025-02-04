package com.angelfg.app.controllers;

import com.angelfg.app.models.Cuenta;
import com.angelfg.app.models.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Excluir en terminal con maven  ./mvnw -Dgroups="!integracion_rt"
@Tag("integracion_rt")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Para poner ordenes en los test (Cual se ejecuta primero, etc)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private Integer puerto;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {

        TransaccionDto dto = new TransaccionDto();
        dto.setMonto(new BigDecimal("100"));
        dto.setCuentaDestinoId(2L);
        dto.setCuentaOrigenId(1L);
        dto.setBancoId(1L);

        String url = crearUrl("/api/cuentas/transferir");
        ResponseEntity<String> response = client.postForEntity(url, dto, String.class);

        System.out.println("PUERTO: " + puerto);

        String json = response.getBody();
        System.out.println("JSON: " + json);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito"));
        assertTrue(json.contains("{\"cuentaOrigenId\":1,\"cuentaDestinoId\":2,\"monto\":100,\"bancoId\":1}"));

        JsonNode jsonNode = this.objectMapper.readTree(json);
        assertEquals("Transferencia realizada con éxito", jsonNode.path("mensaje").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaccion").path("monto").asText());
        assertEquals(1L, jsonNode.path("transaccion").path("cuentaOrigenId").asLong());

        Map<String, Object> response2 = new HashMap<>();
        response2.put("date", LocalDate.now().toString());
        response2.put("status", "OK");
        response2.put("mensaje", "Transferencia realizada con éxito");
        response2.put("transaccion", dto);

        assertEquals(this.objectMapper.writeValueAsString(response2), json);
    }

    @Test
    @Order(2)
    void testDetalle() {

        ResponseEntity<Cuenta> respuesta = client.getForEntity(crearUrl("/api/cuentas/1"), Cuenta.class);
        Cuenta cuenta = respuesta.getBody();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        assertNotNull(cuenta);
        assertEquals(1L, cuenta.getId());
        assertEquals("Luis", cuenta.getPersona());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
        assertEquals(new Cuenta(1L, "Luis", new BigDecimal("900.00")), cuenta);
    }

    @Test
    @Order(3)
    void testListar() throws JsonProcessingException {

        ResponseEntity<Cuenta[]> respuesta = client.getForEntity(crearUrl("/api/cuentas"), Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(respuesta.getBody());

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        assertEquals(2, cuentas.size());
        assertEquals(1L, cuentas.get(0).getId());
        assertEquals("Luis", cuentas.get(0).getPersona());
        assertEquals("900.00", cuentas.get(0).getSaldo().toPlainString());

        assertEquals(2L, cuentas.get(1).getId());
        assertEquals("Andres", cuentas.get(1).getPersona());
        assertEquals("2100.00", cuentas.get(1).getSaldo().toPlainString());

        JsonNode json = this.objectMapper.readTree(this.objectMapper.writeValueAsString(cuentas));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Luis", json.get(0).path("persona").asText());
        assertEquals("900.0", json.get(0).path("saldo").asText());

        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Andres", json.get(1).path("persona").asText());
        assertEquals("2100.0", json.get(1).path("saldo").asText());

    }

    @Test
    @Order(4)
    void testGuardar() {

        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3800"));

        ResponseEntity<Cuenta> respuesta = client.postForEntity(crearUrl("/api/cuentas"), cuenta, Cuenta.class);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        Cuenta cuentaCreada = respuesta.getBody();

        assertNotNull(cuentaCreada);
        assertEquals(3L, cuentaCreada.getId());
        assertEquals("Pepe", cuentaCreada.getPersona());
        assertEquals("3800", cuentaCreada.getSaldo().toPlainString());

    }

    @Test
    @Order(5)
    void testEliminar() {
        ResponseEntity<Cuenta[]> respuesta = client.getForEntity(crearUrl("/api/cuentas"), Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(respuesta.getBody());
        assertEquals(3, cuentas.size());

        // client.delete(crearUrl("/api/cuentas/3"));
        // Alternativa para obtener respuesta
        // ResponseEntity<Void> exchange = client.exchange(crearUrl("/api/cuentas/3"), HttpMethod.DELETE, null, Void.class);

        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);

        ResponseEntity<Void> exchange = client.exchange(
            crearUrl("/api/cuentas/{id}"), HttpMethod.DELETE, null, Void.class, pathVariables
        );
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        respuesta = client.getForEntity(crearUrl("/api/cuentas"), Cuenta[].class);
        cuentas = Arrays.asList(respuesta.getBody());
        assertEquals(2, cuentas.size());

        ResponseEntity<Cuenta> respuestaDetalle = client.getForEntity(crearUrl("/api/cuentas/3"), Cuenta.class);
        assertEquals(HttpStatus.NOT_FOUND, respuestaDetalle.getStatusCode());

        assertFalse(respuestaDetalle.hasBody());
    }

    private String crearUrl(String uri) {
        return "http://localhost:" + puerto + uri;
    }

}
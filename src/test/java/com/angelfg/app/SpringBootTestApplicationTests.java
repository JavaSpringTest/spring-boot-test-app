package com.angelfg.app;

import com.angelfg.app.repositories.BancoRepository;
import com.angelfg.app.repositories.CuentaRepository;
import com.angelfg.app.services.CuentaService;
import com.angelfg.app.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringBootTestApplicationTests {

	private CuentaRepository cuentaRepository;
	private BancoRepository bancoRepository;

	private CuentaService service;

	@BeforeEach
	void setUp() {
		this.cuentaRepository = mock(CuentaRepository.class);
		this.bancoRepository = mock(BancoRepository.class);
		this.service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
	}

	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.CUENTA_001);
		when(cuentaRepository.findById(2L)).thenReturn(Datos.CUENTA_002);
		when(bancoRepository.findById(1L)).thenReturn(Datos.BANCO);

		BigDecimal saldoOrigen = this.service.revisarSaldo(1L);
		BigDecimal saldoDestino = this.service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());
	}

}

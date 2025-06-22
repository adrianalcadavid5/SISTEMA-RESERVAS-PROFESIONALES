package com.reservas.sistematurnos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE, // Sin entorno web
		classes = SistemaTurnosApplication.class // Clase principal
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Desactiva DB embebida
class SistemaTurnosApplicationTests {

	@Test
	void contextLoads() {
	}

}

package com.reservas.sistematurnos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DiagnosticTest {

    @Autowired(required = false)
    private ApplicationContext context;

    @Test
    void checkContext() {
        assertNotNull(context, "¡El contexto de Spring no se cargó!");
        System.out.println("Beans cargados: " + context.getBeanDefinitionCount());
    }
}
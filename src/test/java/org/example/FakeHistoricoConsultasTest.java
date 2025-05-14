package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FakeHistoricoConsultasTest {

    @Test
    void deveRegistrarEConsultarHistorico() {
        HistoricoConsultas historico = new FakeHistoricoConsultas();
        Paciente joana = new Paciente("Joana");

        historico.registrarConsulta(joana, 100.0);
        historico.registrarConsulta(joana, 200.0);

        List<Double> consultas = historico.obterConsultasDoPaciente(joana);
        assertEquals(2, consultas.size());
        assertTrue(consultas.contains(100.0));
        assertTrue(consultas.contains(200.0));
    }
}

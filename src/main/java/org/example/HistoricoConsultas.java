package org.example;

import java.util.List;

public interface HistoricoConsultas {
    void registrarConsulta(Paciente paciente, double valor);
    List<Double> obterConsultasDoPaciente(Paciente paciente);
}

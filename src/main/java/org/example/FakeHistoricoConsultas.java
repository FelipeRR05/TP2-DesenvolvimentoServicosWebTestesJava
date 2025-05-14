package org.example;

import java.util.*;

public class FakeHistoricoConsultas implements HistoricoConsultas {
    private final Map<String, List<Double>> consultasPorPaciente = new HashMap<>();

    @Override
    public void registrarConsulta(Paciente paciente, double valor) {
        consultasPorPaciente
                .computeIfAbsent(paciente.getNome(), k -> new ArrayList<>())
                .add(valor);
    }

    @Override
    public List<Double> obterConsultasDoPaciente(Paciente paciente) {
        return consultasPorPaciente.getOrDefault(paciente.getNome(), List.of());
    }
}

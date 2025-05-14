package org.example;

public class Consulta {
    private final Paciente paciente;
    private final double valor;

    public Consulta(Paciente paciente, double valor) {
        this.paciente = paciente;
        this.valor = valor;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public double getValor() {
        return valor;
    }
}

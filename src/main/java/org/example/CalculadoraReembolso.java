package org.example;

public class CalculadoraReembolso {

    private static final double TETO_REEMBOLSO = 150.0;

    public double calcular(Consulta consulta, PlanoSaude plano, Auditoria auditoria, AutorizadorReembolso autorizador) {
        if (!autorizador.estaAutorizado(consulta.getPaciente())) {
            throw new IllegalStateException("Reembolso não autorizado para este paciente");
        }

        double valorBruto = consulta.getValor() * plano.obterPercentualCobertura();
        double valorFinal = Math.min(valorBruto, TETO_REEMBOLSO);

        auditoria.registrarConsulta(consulta.getPaciente(), consulta.getValor());
        return valorFinal;
    }
}

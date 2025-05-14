package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalculadoraReembolsoTest {

    // Helper para criar um objeto Consulta padrão, evitando repetição de código
    private Consulta criarConsultaPadrao(String nomePaciente, double valor) {
        return new Consulta(new Paciente(nomePaciente), valor);
    }

    // Função de apoio para comparar decimais com margem de erro de 0.01
    private void assertAproximadamenteIgual(double esperado, double atual) {
        double margemErro = 0.01;
        assertTrue(Math.abs(esperado - atual) <= margemErro,
                "Esperado aproximadamente " + esperado + " mas foi " + atual);
    }

    /*
    @Test
    void deveCalcularReembolsoCom70PorCento() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Felipe");

        double reembolso = calc.calcular(dummy, 200.0, 0.70);
        assertEquals(140.0, reembolso, 0.0001);
    }

    @Test
    void deveRetornarZeroSeValorConsultaForZero() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Bernardo");

        double reembolso = calc.calcular(dummy, 0.0, 0.80);
        assertEquals(0.0, reembolso, 0.0001);
    }

    @Test
    void deveRetornarZeroSeCoberturaForZero() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Sofia");

        double reembolso = calc.calcular(dummy, 200.0, 0.0);
        assertEquals(0.0, reembolso, 0.0001);
    }

    @Test
    void deveRetornarValorTotalSeCoberturaFor100PorCento() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Heitor");

        double reembolso = calc.calcular(dummy, 150.0, 1.0);
        assertEquals(150.0, reembolso, 0.0001);
    }

    @Test
    void deveCalcularReembolsoComPlano50PorCento() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Luiza");

        PlanoSaude planoStub = () -> 0.50;

        double reembolso = calc.calcular(dummy, 100.0, planoStub);
        assertEquals(50.0, reembolso, 0.0001);
    }

    @Test
    void deveCalcularReembolsoComPlano80PorCento() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Guilherme");

        PlanoSaude planoStub = () -> 0.80;

        double reembolso = calc.calcular(dummy, 150.0, planoStub);
        assertEquals(120.0, reembolso, 0.0001);
    }

    static class SpyAuditoria implements Auditoria {
        boolean chamado = false;
        Paciente pacienteChamado;
        double valorChamado;

        @Override
        public void registrarConsulta(Paciente paciente, double valor) {
            chamado = true;
            pacienteChamado = paciente;
            valorChamado = valor;
        }

        boolean foiChamado() {
            return chamado;
        }
    }

    @Test
    void deveChamarAuditoriaAoCalcularReembolso() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Paciente dummy = new Paciente("Lucas");

        PlanoSaude planoStub = () -> 0.75;
        SpyAuditoria spy = new SpyAuditoria();

        double reembolso = calc.calcular(dummy, 100.0, planoStub, spy);

        assertTrue(spy.foiChamado(), "A auditoria deve ser chamada");
        assertEquals(dummy, spy.pacienteChamado);
        assertEquals(100.0, spy.valorChamado, 0.0001);
        assertEquals(75.0, reembolso, 0.0001);
    }
    */
    @Test
    void deveLancarExcecaoSeNaoAutorizado() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Consulta consulta = criarConsultaPadrao("Invalido", 100.0);

        PlanoSaude planoStub = () -> 0.70; // Stub simula plano com 70% de cobertura
        Auditoria auditoriaStub = mock(Auditoria.class); // Mock sem comportamento necessário aqui
        AutorizadorReembolso autorizadorMock = mock(AutorizadorReembolso.class); // Mock simula negativa de autorização

        when(autorizadorMock.estaAutorizado(consulta.getPaciente())).thenReturn(false);

        // Testa se exceção é lançada quando reembolso não é autorizado
        assertThrows(IllegalStateException.class, () -> {
            calc.calcular(consulta, planoStub, auditoriaStub, autorizadorMock);
        });
    }

    @Test
    void deveCalcularReembolsoSeAutorizado() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Consulta consulta = criarConsultaPadrao("Valido", 100.0);

        PlanoSaude planoStub = () -> 0.60; // Stub simula plano com 60%
        Auditoria auditoriaStub = mock(Auditoria.class);
        AutorizadorReembolso autorizadorMock = mock(AutorizadorReembolso.class);
        when(autorizadorMock.estaAutorizado(consulta.getPaciente())).thenReturn(true);

        // Verifica se o valor do reembolso está correto
        double reembolso = calc.calcular(consulta, planoStub, auditoriaStub, autorizadorMock);
        assertEquals(60.0, reembolso, 0.0001);
    }

    @Test
    void deveCalcularComPlano80PorCentoUsandoHelper() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Consulta consulta = criarConsultaPadrao("Guilherme", 150.0);

        PlanoSaude planoStub = () -> 0.80;
        Auditoria auditoriaStub = mock(Auditoria.class);
        AutorizadorReembolso autorizadorStub = mock(AutorizadorReembolso.class);
        when(autorizadorStub.estaAutorizado(consulta.getPaciente())).thenReturn(true);

        double reembolso = calc.calcular(consulta, planoStub, auditoriaStub, autorizadorStub);
        assertAproximadamenteIgual(120.0, reembolso); // Valida cálculo com margem
    }

    @Test
    void deveAplicarTetoDe150Reais() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Consulta consulta = criarConsultaPadrao("Carlos", 500.0); // Valor muito alto → teto aplicado

        PlanoSaude planoStub = () -> 0.80;
        Auditoria auditoriaStub = mock(Auditoria.class);
        AutorizadorReembolso autorizadorStub = mock(AutorizadorReembolso.class);
        when(autorizadorStub.estaAutorizado(consulta.getPaciente())).thenReturn(true);

        // Valida aplicação do teto de R$150
        double reembolso = calc.calcular(consulta, planoStub, auditoriaStub, autorizadorStub);
        assertAproximadamenteIgual(150.0, reembolso);
    }

    @Test
    void naoAplicaTetoSeReembolsoForMenorQue150() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Consulta consulta = criarConsultaPadrao("Mariana", 100.0);

        PlanoSaude planoStub = () -> 0.70; // Deve resultar em R$70 (sem aplicar teto)
        Auditoria auditoriaStub = mock(Auditoria.class);
        AutorizadorReembolso autorizadorStub = mock(AutorizadorReembolso.class);
        when(autorizadorStub.estaAutorizado(consulta.getPaciente())).thenReturn(true);

        double reembolso = calc.calcular(consulta, planoStub, auditoriaStub, autorizadorStub);
        assertAproximadamenteIgual(70.0, reembolso);
    }

    @Test
    void deveCalcularReembolsoCompletoIntegrado() {
        CalculadoraReembolso calc = new CalculadoraReembolso();
        Consulta consulta = criarConsultaPadrao("Amanda", 250.0); // 80% de 250 = 200 → aplica teto

        PlanoSaude planoStub = () -> 0.80;

        // Mock do autorizador retorna true
        AutorizadorReembolso autorizadorMock = mock(AutorizadorReembolso.class);
        when(autorizadorMock.estaAutorizado(consulta.getPaciente())).thenReturn(true);

        // Spy interno para verificar se auditoria foi chamada corretamente
        class SpyAuditoria implements Auditoria {
            boolean chamado = false;

            @Override
            public void registrarConsulta(Paciente paciente, double valor) {
                chamado = true;
                assertEquals("Amanda", paciente.getNome());
                assertEquals(250.0, valor, 0.01);
            }
        }

        SpyAuditoria spy = new SpyAuditoria();

        double reembolso = calc.calcular(consulta, planoStub, spy, autorizadorMock);

        // Valida que o spy foi acionado e o valor com teto aplicado
        assertTrue(spy.chamado, "Auditoria deve ter sido chamada");
        assertAproximadamenteIgual(150.0, reembolso);
    }
}
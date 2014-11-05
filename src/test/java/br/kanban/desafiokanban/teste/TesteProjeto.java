package br.kanban.desafiokanban.teste;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import br.kanban.desafiokanban.core.Cronograma;
import br.kanban.desafiokanban.entidade.Demanda;
import br.kanban.desafiokanban.entidade.Demanda.Tipo;
import br.kanban.desafiokanban.entidade.Empregado;
import br.kanban.desafiokanban.entidade.Empregado.Sexo;
import br.kanban.desafiokanban.entidade.FaseDemanda;
import br.kanban.desafiokanban.entidade.PapelEmpregado;
import br.kanban.desafiokanban.entidade.Projeto;

public class TesteProjeto extends TestCase {
	private Empregado desenvolvedor;
	private Empregado analistaNegocio;
	private Demanda demandaEstoria;
	private Projeto projetoA;
	
	@Override
	public void setUp() {
		criarProjetoComUmaEstoriaDeEsforco2();
		criarDesenvolvedor();
		criarAnalistaNegocio();
	}

	private void criarDesenvolvedor() {
		desenvolvedor = new Empregado("Mariane", Sexo.Feminino,
				PapelEmpregado.Desenvolvedor, 400);
		desenvolvedor.print();
	}

	private void criarAnalistaNegocio() {
		analistaNegocio = new Empregado("Cláudio", Sexo.Masculino,
				PapelEmpregado.AnalistaNegocio, 400);
		analistaNegocio.print();
	}

	private void criarProjetoComUmaEstoriaDeEsforco2() {
		Map<FaseDemanda, Integer> faseEsforcos = new HashMap<FaseDemanda, Integer>();
		faseEsforcos.put(FaseDemanda.Analysis, 2);

		demandaEstoria = new Demanda("1A", Tipo.Estoria, faseEsforcos);
		demandaEstoria.setFaseAtual(FaseDemanda.Analysis);
		demandaEstoria.print();

		projetoA = new Projeto("A", "Crédito Especial", 500);
		projetoA.adicionarDemanda(demandaEstoria);
		projetoA.print();
	}

	public void testeConclusaoProjeto() {
		Cronograma.avancarDia();
		analistaNegocio.setEsforcosDisponiveisDia(1);
		analistaNegocio.executarDemanda(demandaEstoria);
	
		assertTrue(!demandaEstoria.estaConcluida());
		assertTrue(!projetoA.estaConcluido());

		Cronograma.avancarDia();
		analistaNegocio.setEsforcosDisponiveisDia(2);
		analistaNegocio.executarDemanda(demandaEstoria);
	
		assertTrue(demandaEstoria.estaConcluida());
		assertTrue(projetoA.estaConcluido());

	}
}

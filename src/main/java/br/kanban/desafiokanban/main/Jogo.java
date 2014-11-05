package br.kanban.desafiokanban.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.core.BacklogManager;
import br.kanban.desafiokanban.core.Cronograma;
import br.kanban.desafiokanban.core.EmpregadosManager;
import br.kanban.desafiokanban.core.KanbanManager;
import br.kanban.desafiokanban.core.PortfolioManager;
import br.kanban.desafiokanban.entidade.Empregado;
import br.kanban.desafiokanban.entidade.PapelEmpregado;
import br.kanban.desafiokanban.entidade.Projeto;
import br.kanban.desafiokanban.entidade.Empregado.Sexo;

public class Jogo {
	private static final Logger LOGGER = LogManager.getLogger(Jogo.class);
	private static Jogo instance;

	private BacklogManager backlogManager;
	private KanbanManager kanbanManager;
	private PortfolioManager portfolioManager;
	private EmpregadosManager empregadosManager;

	public static Jogo getInstance() {
		if (instance == null) {
			instance = new Jogo();
		}

		return instance;
	}

	private Jogo() {
		empregadosManager = EmpregadosManager.getInstance();
		portfolioManager = PortfolioManager.getInstance();
		backlogManager = BacklogManager.getInstance();
		kanbanManager = KanbanManager.getInstance();
	}

	public void executarDia(Integer dia) {
		if (dia % 3 == 0) {
			// A cada 3 dias eh dia de deploy por um *desenvolvedor
		} else if (dia % 5 == 0) {
			// A cada 5 dias contabilizam-se as metricas e compram-se kaizens
		}
		empregadosManager.distribuirEsforcosEmpregados();
		kanbanManager.executar();
	}

	private boolean acabou() {
		return !portfolioManager.existemProjetosNaoConcluidos();
	}

	private void preparar() {
		construirEmpregados();
		construirPortfolioInicial();
		construirBacklogInicial();
		planejarProximoDia();
	}

	private void construirBacklogInicial() {
		backlogManager.acrescentarDemandasNovosProjetos();
		backlogManager.print();
	}

	private void construirEmpregados() {
		Empregado claudio = new Empregado("Cláudio", Sexo.Masculino,
				PapelEmpregado.AnalistaNegocio, 400);
		List<Empregado> analistasNegocio = new ArrayList<Empregado>();
		analistasNegocio.add(claudio);
		empregadosManager.adicionarEmpregados(PapelEmpregado.AnalistaNegocio,
				analistasNegocio);

		Empregado alisson = new Empregado("Alisson", Sexo.Masculino,
				PapelEmpregado.Desenvolvedor, 500);
		List<Empregado> desenvolvedores = new ArrayList<Empregado>();
		desenvolvedores.add(alisson);
		empregadosManager.adicionarEmpregados(PapelEmpregado.Desenvolvedor,
				desenvolvedores);

		Empregado patricia = new Empregado("Patrícia", Sexo.Feminino,
				PapelEmpregado.AnalistaQualidade, 400);
		List<Empregado> analistasQualidade = new ArrayList<Empregado>();
		analistasQualidade.add(patricia);
		empregadosManager.adicionarEmpregados(PapelEmpregado.AnalistaQualidade,
				analistasQualidade);

		Empregado mariane = new Empregado("Mariane", Sexo.Feminino,
				PapelEmpregado.Deployer, 300);
		List<Empregado> deployers = new ArrayList<Empregado>();
		deployers.add(mariane);
		empregadosManager.adicionarEmpregados(PapelEmpregado.Deployer,
				deployers);

		empregadosManager.print();
	}

	private void construirPortfolioInicial() {
		Projeto projetoA = new Projeto("A", "Mensagem instantânea", 400);
		projetoA.adicionarDemanda(FabricaDemanda.criarDemandaEstoria("1A"));
		projetoA.adicionarDemanda(FabricaDemanda.criarDemandaEstoria("2A"));
		projetoA.adicionarDemanda(FabricaDemanda.criarDemandaBug("3A"));

//		Projeto projetoB = new Projeto("B", "Rede social", 800);
//		projetoB.adicionarDemanda(FabricaDemanda.criarDemandaEstoria("1B"));
//		projetoB.adicionarDemanda(FabricaDemanda.criarDemandaEstoria("2B"));
//		projetoB.adicionarDemanda(FabricaDemanda.criarDemandaEstoria("3B"));
//		projetoB.adicionarDemanda(FabricaDemanda.criarDemandaBug("4B"));
//		projetoB.adicionarDemanda(FabricaDemanda.criarDemandaEstoria("5B"));
//		projetoB.adicionarDemanda(FabricaDemanda.criarDemandaBug("6B"));

		portfolioManager.adicionarProjeto(projetoA);
//		portfolioManager.adicionarProjeto(projetoB);
		
		portfolioManager.print();
	}

	public static void main(String[] args) throws InterruptedException {
		LOGGER.info("Preparando o jogo...");
		Jogo jogo = new Jogo();
		jogo.preparar();
		LOGGER.info("O jogo começou!");
		while (!jogo.acabou()) {
			Cronograma.avancarDia();
			jogo.executarDia(Cronograma.getDia());
			jogo.planejarProximoDia();
			Thread.sleep(1000);
		}
	}

	private void planejarProximoDia() {
		kanbanManager.buscarDemandasBacklog();
	}

}

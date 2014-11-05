package br.kanban.desafiokanban.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.entidade.Demanda;
import br.kanban.desafiokanban.entidade.Empregado;
import br.kanban.desafiokanban.entidade.FaseDemanda;
import br.kanban.desafiokanban.entidade.PapelEmpregado;

public class KanbanManager {
	private static final Logger LOGGER = LogManager
			.getLogger(KanbanManager.class);
	private static final Integer DEFAULT_WIP = 4;
	private static KanbanManager instance;
	private EmpregadosManager empregadosManager;
	private BacklogManager backlogManager;

	private Map<FaseDemanda, ListaDemanda> faseListaDemandas = new HashMap<FaseDemanda, ListaDemanda>();
	private Map<PapelEmpregado, FaseDemanda> papelFase = new HashMap<PapelEmpregado, FaseDemanda>();

	private KanbanManager() {
		empregadosManager = EmpregadosManager.getInstance();
		backlogManager = BacklogManager.getInstance();
		construirListaDemandasPorFaseDemanda();
		construirMapeamentoPapelEmpregadoFaseDemanda();

		LOGGER.debug("Novo KanbanManager criado.");
	}

	private void construirMapeamentoPapelEmpregadoFaseDemanda() {
		papelFase.put(PapelEmpregado.AnalistaNegocio, FaseDemanda.Analysis);
		papelFase.put(PapelEmpregado.Desenvolvedor, FaseDemanda.Development);
		papelFase.put(PapelEmpregado.AnalistaQualidade, FaseDemanda.Testing);
		papelFase.put(PapelEmpregado.Deployer, FaseDemanda.DeployReady);
	}

	private void construirListaDemandasPorFaseDemanda() {
		faseListaDemandas.put(FaseDemanda.Analysis, new ListaDemanda(
				DEFAULT_WIP));
		faseListaDemandas.put(FaseDemanda.Development, new ListaDemanda(
				DEFAULT_WIP));
		faseListaDemandas.put(FaseDemanda.Testing,
				new ListaDemanda(DEFAULT_WIP));
		faseListaDemandas.put(FaseDemanda.DeployReady, new ListaDemanda(
				DEFAULT_WIP));
		faseListaDemandas.put(FaseDemanda.Deployed, new ListaDemanda(
				Integer.MAX_VALUE));
	}

	public static KanbanManager getInstance() {
		if (instance == null) {
			instance = new KanbanManager();
		}

		return instance;
	}

	public boolean adicionarDemanda(Demanda demanda) {
		ListaDemanda demandas = faseListaDemandas.get(FaseDemanda.Analysis);
		return demandas.add(demanda);
	}

	public void configurarWip(FaseDemanda fase, Integer wip) {
		ListaDemanda demandas = faseListaDemandas.get(fase);
		demandas.setMaxSize(wip);
	}

	public Integer recuperarWip(FaseDemanda fase) {
		return faseListaDemandas.get(fase).getMaxSize();
	}

	public void executar() {
		executarEAvancarDemandas(PapelEmpregado.AnalistaQualidade);
		executarEAvancarDemandas(PapelEmpregado.Desenvolvedor);
		executarEAvancarDemandas(PapelEmpregado.AnalistaNegocio);
		executarEAvancarDemandas(PapelEmpregado.Deployer);
	}

	private void executarEAvancarDemandas(PapelEmpregado papel) {
		List<Empregado> empregados = empregadosManager.getEmpregados(papel);
		if (empregados != null) {
			for (Empregado empregado : empregados) {
				empregado.trabalhar();
			}
		}
		avancarDemandas(papelFase.get(papel));
	}

	public void buscarDemandasBacklog() {
		ListaDemanda demandasAnalise = faseListaDemandas
				.get(FaseDemanda.Analysis);
		List<Demanda> demandas = backlogManager.removerDemandas(demandasAnalise
				.vagasDisponiveis());
		for (Demanda demanda : demandas) {
			demanda.setFaseAtual(FaseDemanda.Analysis);
			demandasAnalise.add(demanda);
		}
	}

	private void avancarDemandas(FaseDemanda fase) {
		ListaDemanda demandasDaFase = faseListaDemandas.get(fase);
		List<Demanda> demandasARemover = new ArrayList<Demanda>();
		for (Demanda demanda : demandasDaFase) {
			if (demanda.faseAtualEstaConcluida()) {
				FaseDemanda proximaFase = proximaFase(fase);
				boolean demandaAvancou = faseListaDemandas.get(proximaFase)
						.add(demanda);
				if (demandaAvancou) {
					demanda.setFaseAtual(proximaFase);
					demandasARemover.add(demanda);
				}
			}
		}
		demandasDaFase.removeAll(demandasARemover);
	}

	private FaseDemanda proximaFase(FaseDemanda fase) {
		switch (fase) {
		case Backlog:
			return FaseDemanda.Analysis;
		case Analysis:
			return FaseDemanda.Development;
		case Development:
			return FaseDemanda.Testing;
		case Testing:
			return FaseDemanda.DeployReady;
		case DeployReady:
			return FaseDemanda.Deployed;
		default:
			return null;
		}
	}

	public ListaDemanda recuperarDemandas(PapelEmpregado papel) {
		return faseListaDemandas.get(papelFase.get(papel));
	}

	public boolean existemDemandasEmExecucao() {
		Set<FaseDemanda> fasesDemandas = faseListaDemandas.keySet();
		for (FaseDemanda faseDemanda : fasesDemandas) {
			if (faseDemanda.equals(FaseDemanda.Deployed)) {
				continue;
			}
			ListaDemanda demandasFase = faseListaDemandas.get(faseDemanda);
			if (!demandasFase.isEmpty()) {
				return true;
			}
		}
		return false;
	}

}

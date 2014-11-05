package br.kanban.desafiokanban.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.entidade.Demanda;
import br.kanban.desafiokanban.excecao.NaoHaMaisProjetosException;

public class BacklogManager {
	private static final Logger LOGGER = LogManager
			.getLogger(BacklogManager.class);

	private PortfolioManager portfolioManager;
	private List<Demanda> backlogDemandas = new ArrayList<Demanda>();

	private static BacklogManager instance;

	public static BacklogManager getInstance() {
		if (instance == null) {
			instance = new BacklogManager();
		}

		return instance;
	}

	private BacklogManager() {
		portfolioManager = PortfolioManager.getInstance();

		LOGGER.debug("Novo BacklogManager criado.");
	}

	public void acrescentarDemandasNovosProjetos() {
		try {
			backlogDemandas.addAll(portfolioManager.iniciarDemandasProjeto());
		} catch (NaoHaMaisProjetosException e) {
			LOGGER.debug(
					"Não foi possível incrementar o backlog pois não havia mais projetos pendentes",
					e);
		}
	}

	public boolean existemDemandasNaoIniciadas() {
		return !backlogDemandas.isEmpty();
	}

	public List<Demanda> removerDemandas(Integer maxDemandas) {
		maxDemandas = (maxDemandas > backlogDemandas.size() ? backlogDemandas
				.size() : maxDemandas);
		List<Demanda> demandas = new ArrayList<Demanda>();
		for (int i = maxDemandas-1; i >= 0; i--) {
			demandas.add(backlogDemandas.remove(i));
		}
		return demandas;
	}

	public void print() {
		for (Demanda demanda : backlogDemandas) {
			demanda.print();
		}
	}

}

package br.kanban.desafiokanban.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.entidade.Demanda;
import br.kanban.desafiokanban.entidade.Projeto;
import br.kanban.desafiokanban.excecao.NaoHaMaisProjetosException;

public class PortfolioManager {
	private static final Logger LOGGER = LogManager
			.getLogger(PortfolioManager.class);

	private static PortfolioManager instance;

	private List<Projeto> projetos = new ArrayList<Projeto>();
	private int indiceProximoProjeto = -1;

	public static PortfolioManager getInstance() {
		if (instance == null) {
			instance = new PortfolioManager();
		}

		return instance;
	}

	private PortfolioManager() {
		LOGGER.debug("Novo PortfolioManager criado.");
	}

	public void adicionarProjeto(Projeto projeto) {
		projetos.add(projeto);
	}

	private Projeto recuperarProximoProjeto() throws NaoHaMaisProjetosException {
		++indiceProximoProjeto;

		if (indiceProximoProjeto >= projetos.size()) {
			throw new NaoHaMaisProjetosException();
		}

		return projetos.get(indiceProximoProjeto);
	}

	public List<Demanda> iniciarDemandasProjeto() throws NaoHaMaisProjetosException {
		Projeto projeto = recuperarProximoProjeto();
		return projeto.iniciarExecucao();
	}

	public boolean existemProjetosNaoConcluidos() {
		for (Projeto projeto : projetos) {
			if (!projeto.estaConcluido()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void print() {
		for (Projeto projeto : projetos) {
			projeto.print();
		}
	}
	
}
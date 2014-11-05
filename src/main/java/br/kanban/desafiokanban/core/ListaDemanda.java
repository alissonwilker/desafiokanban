package br.kanban.desafiokanban.core;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.entidade.Demanda;

public class ListaDemanda extends ArrayList<Demanda> {
	private static final Logger LOGGER = LogManager
			.getLogger(ListaDemanda.class);

	private static final long serialVersionUID = 1L;
	private Integer maxSize;

	public ListaDemanda(Integer maxSize) {
		setMaxSize(maxSize);
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	@Override
	public boolean add(Demanda demanda) {
		if (this.size() >= maxSize) {
			LOGGER.debug("Lista demanda já está completa. Não foi possível adicionar nova demanda "
					+ demanda.getId() + ".");
			return false;
		} 
		
		return super.add(demanda);
	}

	public Integer vagasDisponiveis() {
		return maxSize - size();
	}
}

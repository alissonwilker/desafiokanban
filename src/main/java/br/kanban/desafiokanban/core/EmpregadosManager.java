package br.kanban.desafiokanban.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.entidade.Empregado;
import br.kanban.desafiokanban.entidade.PapelEmpregado;

public class EmpregadosManager {
	private static final Logger LOGGER = LogManager
			.getLogger(EmpregadosManager.class);

	private Map<PapelEmpregado, List<Empregado>> papelEmpregados = new HashMap<PapelEmpregado, List<Empregado>>();

	private static EmpregadosManager instance;

	public static EmpregadosManager getInstance() {
		if (instance == null) {
			instance = new EmpregadosManager();
		}

		return instance;
	}

	private EmpregadosManager() {
		LOGGER.debug("Novo EmpregadosManager criado.");
	}

	public List<Empregado> getEmpregados(PapelEmpregado papel) {
		return papelEmpregados.get(papel);
	}

	public void adicionarEmpregados(PapelEmpregado papel,
			List<Empregado> empregados) {
		papelEmpregados.put(papel, empregados);
	}

	public void print() {
		for (PapelEmpregado papel : papelEmpregados.keySet()) {
			List<Empregado> empregados = papelEmpregados.get(papel);
			if (empregados != null) {
				for (Empregado empregado : empregados) {
					empregado.print();
				}
			}
		}
	}

	public void distribuirEsforcosEmpregados() {
		for (PapelEmpregado papel : papelEmpregados.keySet()) {
			List<Empregado> empregados = papelEmpregados.get(papel);
			if (empregados != null) {
				for (Empregado empregado : empregados) {
					empregado.resetEsforcosDisponiveisDia();
				}
			}
		}
	}

}

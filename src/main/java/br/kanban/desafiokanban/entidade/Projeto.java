package br.kanban.desafiokanban.entidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.core.Cronograma;

public class Projeto extends Observable implements Observer {
	private static final Logger LOGGER = LogManager.getLogger(Projeto.class);

	private String id;
	private String nome;
	private Integer estimativaDemandas;
	private Integer diaEntradaPortfolio;
	private Integer diaConclusao;
	private Integer custoAtrasoSemanal;
	private Integer diaInicioCustoAtrasoSemanal;
	private List<Demanda> demandasPendentes = new ArrayList<Demanda>();
	private Status status;

	private enum Status {
		Planejado, EmExecucao, Concluido
	}

	public Projeto(String id, String nome, Integer custoAtrasoSemanal) {
		this.id = id;
		this.nome = nome;
		this.custoAtrasoSemanal = custoAtrasoSemanal;
		this.status = Status.Planejado;

		LOGGER.debug("Novo projeto " + this.nome + " criado.");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void adicionarDemanda(Demanda demanda) {
		demanda.addObserver(this);
		demandasPendentes.add(demanda);
	}

	public List<Demanda> iniciarExecucao() {
		this.status = Status.EmExecucao;
		this.diaEntradaPortfolio = Cronograma.getDia();
		return demandasPendentes;
	}

	public void adicionarDemandas(Collection<Demanda> demandas) {
		for (Demanda demanda : demandas) {
			adicionarDemanda(demanda);
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getEstimativaDemandas() {
		return estimativaDemandas;
	}

	public void setEstimativaDemandas(Integer estimativaDemandas) {
		this.estimativaDemandas = estimativaDemandas;
	}

	public Integer getDiaEntradaPortfolio() {
		return diaEntradaPortfolio;
	}

	public void setDiaEntradaPortfolio(Integer diaEntradaPortfolio) {
		this.diaEntradaPortfolio = diaEntradaPortfolio;
	}

	public Integer getDiaConclusao() {
		return diaConclusao;
	}

	public void setDiaConclusao(Integer diaConclusao) {
		this.diaConclusao = diaConclusao;
	}

	public Integer getCustoAtrasoSemanal() {
		return custoAtrasoSemanal;
	}

	public void setCustoAtrasoSemanal(Integer custoAtrasoSemanal) {
		this.custoAtrasoSemanal = custoAtrasoSemanal;
	}

	public Integer getDiaInicioCustoAtrasoSemanal() {
		return diaInicioCustoAtrasoSemanal;
	}

	public void setDiaInicioCustoAtrasoSemanal(
			Integer diaInicioCustoAtrasoSemanal) {
		this.diaInicioCustoAtrasoSemanal = diaInicioCustoAtrasoSemanal;
	}

	public void setDemandasPendentes(List<Demanda> demandasPendentes) {
		this.demandasPendentes = demandasPendentes;
	}

	@Override
	public void update(Observable demanda, Object arg1) {
		demandasPendentes.remove(demanda);
		if (estaConcluido()) {
			this.status = Status.Concluido;
			setChanged();
			notifyObservers();
		}
	}

	public Status getStatus() {
		return this.status;
	}

	public boolean estaConcluido() {
		return demandasPendentes.isEmpty();
	}

	public void print() {
		System.out.printf("---%nProjeto %s%n" + "%s%n" + "(~%s demandas)%n"
				+ "Entrada no portfólio: %s%n" + "Dia concluído: %s%n"
				+ "Custo do atraso (por semana): %s%n"
				+ "Custo de atraso a partir do dia: %s%n", this.id, this.nome,
				this.demandasPendentes.size(), this.diaEntradaPortfolio,
				this.diaConclusao, this.custoAtrasoSemanal,
				this.diaInicioCustoAtrasoSemanal);
	}
}

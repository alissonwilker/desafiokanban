package br.kanban.desafiokanban.entidade;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.core.KanbanManager;
import br.kanban.desafiokanban.core.ListaDemanda;

public class Empregado {
	private static final Logger LOGGER = LogManager.getLogger(Empregado.class);

	private static final Integer DEFAULT_ESFORCO_DIARIO = 2;

	private String nome;
	private Set<PapelEmpregado> papeis = new HashSet<PapelEmpregado>();
	private PapelEmpregado papelAtual;
	private Integer salarioSemanal;
	private Sexo sexo;
	private Integer esforcosDisponiveisDia = 0;
	private KanbanManager kanbanManager;

	public enum Sexo {
		Masculino, Feminino
	}

	public Empregado(String nome, Sexo sexo, PapelEmpregado papelAtual,
			Integer salarioSemanal) {
		this.nome = nome;
		this.sexo = sexo;
		this.papeis.add(papelAtual);
		this.papelAtual = papelAtual;
		this.salarioSemanal = salarioSemanal;
		this.resetEsforcosDisponiveisDia();
		this.kanbanManager = KanbanManager.getInstance();

		LOGGER.debug("Novo empregado " + this.nome + " criado.");
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public PapelEmpregado getPapelAtual() {
		return papelAtual;
	}

	public void setPapelAtual(PapelEmpregado papelAtual) {
		this.papelAtual = papelAtual;
	}

	public Set<PapelEmpregado> getPapeis() {
		return papeis;
	}

	public void setPapeis(Set<PapelEmpregado> papeis) {
		this.papeis = papeis;
	}

	public boolean temPapel(PapelEmpregado papel) {
		return this.papeis.contains(papel);
	}

	public void adicionarPapel(PapelEmpregado papel) {
		this.papeis.add(papel);
	}

	public Integer getSalarioSemanal() {
		return salarioSemanal;
	}

	public void setSalarioSemanal(Integer salarioSemanal) {
		this.salarioSemanal = salarioSemanal;
	}

	public Integer getEsforcosDisponiveisDia() {
		return esforcosDisponiveisDia;
	}

	public void setEsforcosDisponiveisDia(Integer esforcosDisponiveisDia) {
		this.esforcosDisponiveisDia = esforcosDisponiveisDia;
	}

	public void executarDemanda(Demanda demanda) {
		esforcosDisponiveisDia -= demanda.executar(esforcosDisponiveisDia);
	}

	public void trabalhar() {
		ListaDemanda demandas = kanbanManager.recuperarDemandas(papelAtual);
		for (Demanda demanda : demandas) {
			executarDemanda(demanda);
		}
	}

	public void print() {
		System.out.printf("---%nEmpregado %s%n" + "%s%n" + "R$ %s/semana%n"
				+ "Papéis: ", this.nome, this.papelAtual,
				this.salarioSemanal);
		for (PapelEmpregado papelEmpregado : papeis) {
			System.out.printf("%s, ", papelEmpregado);
		}
		System.out.printf("%n");
	}

	public void resetEsforcosDisponiveisDia() {
		setEsforcosDisponiveisDia(DEFAULT_ESFORCO_DIARIO);
	}
}

package br.kanban.desafiokanban.entidade;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.core.Cronograma;

public class Demanda extends Observable {
	private static final Logger LOGGER = LogManager.getLogger(Demanda.class);

	private String id;
	private Integer diaInicio;
	private Integer diaDeploy;
	private Integer custo = 0;
	private Tipo tipo;
	private Map<FaseDemanda, Integer> faseEsforcos = new HashMap<FaseDemanda, Integer>();
	private FaseDemanda faseAtual;

	// TODO Modelar outros tipos de demanda (treinamento, contratacao, demissao)
	public enum Tipo {
		Estoria, Bug, BugCritico, KaizenTestesAutomaticos, KaizenOneClickDeploy, KaizenContinuousDelivery
	}

	public Demanda(String id, Tipo tipo, Map<FaseDemanda, Integer> faseEsforcos) {
		this.id = id;
		this.tipo = tipo;
		this.faseEsforcos = faseEsforcos;
		this.faseEsforcos.put(FaseDemanda.DeployReady, 0);
		this.faseAtual = FaseDemanda.Backlog;

		LOGGER.debug("Nova demanda " + tipo + " " + id + " criada.");
	}
	
	public void setFaseAtual(FaseDemanda fase) {
		this.faseAtual = fase;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDiaInicio() {
		return diaInicio;
	}

	public void setDiaInicio(Integer diaInicio) {
		this.diaInicio = diaInicio;
	}

	public Integer getDiaDeploy() {
		return diaDeploy;
	}

	public void setDiaDeploy(Integer diaDeploy) {
		this.diaDeploy = diaDeploy;
	}

	public Integer getCusto() {
		return custo;
	}

	public void setCusto(Integer custo) {
		this.custo = custo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Map<FaseDemanda, Integer> getFaseEsforcos() {
		return faseEsforcos;
	}

	public void setFaseEsforcos(Map<FaseDemanda, Integer> faseEsforcos) {
		this.faseEsforcos = faseEsforcos;
	}

	public Integer executar(Integer esforcosEmpregado) {
		registrarDiaInicio();

		Integer esforcosRealizados;
		Integer esforcosFase = faseEsforcos.get(faseAtual);
		Integer deltaEsforcos = esforcosFase - esforcosEmpregado;

		if (deltaEsforcos >= 0) {
			faseEsforcos.put(faseAtual, deltaEsforcos);
			esforcosRealizados = esforcosEmpregado;
		} else {
			faseEsforcos.put(faseAtual, 0);
			esforcosRealizados = esforcosFase;
		}

		notificarObserversSeConcluida(esforcosRealizados);

		print();
		
		return esforcosRealizados;
	}

	private void notificarObserversSeConcluida(Integer esforcosRealizados) {
		if ((esforcosRealizados > 0) && estaConcluida()) {
			LOGGER.debug("A demanda " + id
					+ " foi concluída está pronta para deploy.");
			setChanged();
			notifyObservers();
		}
	}

	private void registrarDiaInicio() {
		if (diaInicio == null) {
			diaInicio = Cronograma.getDia();
		}
	}

	public void deploy() {
		diaDeploy = Cronograma.getDia();

		LOGGER.debug("A demanda " + id + " foi entregue no dia " + diaDeploy
				+ "!");
	}
	
	public boolean faseAtualEstaConcluida() {
		Integer esforcosFase = faseEsforcos.get(faseAtual);
		return esforcosFase == 0;
	}

	public boolean estaConcluida() {
		Collection<Integer> esforcos = faseEsforcos.values();

		for (Integer esforco : esforcos) {
			// se qualquer esforco for negativo alguma coisa deu errado
			assert esforco >= 0;

			if (esforco > 0) {
				return false;
			}
		}

		return true;
	}

	public void print() {
		System.out.printf("---%nDemanda %s%n" + "%s%n" + "%s / %s%n"
				+ "%s | %s | %s%n", this.id, this.tipo, this.diaInicio,
				this.diaDeploy, this.faseEsforcos.get(FaseDemanda.Analysis),
				this.faseEsforcos.get(FaseDemanda.Development),
				this.faseEsforcos.get(FaseDemanda.Testing));
	}
}

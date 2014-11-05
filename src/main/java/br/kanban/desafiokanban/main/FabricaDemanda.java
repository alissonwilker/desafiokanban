package br.kanban.desafiokanban.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.kanban.desafiokanban.entidade.Demanda;
import br.kanban.desafiokanban.entidade.Demanda.Tipo;
import br.kanban.desafiokanban.entidade.FaseDemanda;

public class FabricaDemanda {
	private static final Logger LOGGER = LogManager
			.getLogger(FabricaDemanda.class);

	public static Demanda criarDemandaEstoria(String id) {
		Map<FaseDemanda, Integer> faseEsforcos = criarFaseEsforcos(2, 4, 2);
		Demanda estoria = new Demanda(id, Tipo.Estoria, faseEsforcos);

		LOGGER.debug("Nova demanda Estória " + id + " criada.");

		return estoria;
	}

	public static Demanda criarDemandaBug(String id) {
		Map<FaseDemanda, Integer> faseEsforcos = criarFaseEsforcos(1, 3, 1);
		Demanda bug = new Demanda(id, Tipo.Bug, faseEsforcos);

		LOGGER.debug("Nova demanda Bug " + id + " criada.");

		return bug;
	}

	public static Demanda criarDemandaBugCritico(String id) {
		Map<FaseDemanda, Integer> faseEsforcos = criarFaseEsforcos(2, 4, 5);
		Demanda bugCritico = new Demanda(id, Tipo.Bug, faseEsforcos);
		bugCritico.setCusto(100);

		LOGGER.debug("Nova demanda Bug Crítico " + id + " criada.");

		return bugCritico;
	}

	public static Demanda criarDemandaKaizenTestesAutomaticos() {
		Map<FaseDemanda, Integer> faseEsforcos = criarFaseEsforcos(3, 3, 3);

		Demanda kaizenTestes = new Demanda(Tipo.KaizenTestesAutomaticos.name(),
				Tipo.KaizenTestesAutomaticos, faseEsforcos);
		kaizenTestes.setCusto(700);

		LOGGER.debug("Nova demanda Kaizen Testes criada.");

		return kaizenTestes;
	}

	public static Demanda criarDemandaKaizenOneClickDeploy() {
		Map<FaseDemanda, Integer> faseEsforcos = criarFaseEsforcos(3, 3, 3);

		Demanda kaizenOneClick = new Demanda(Tipo.KaizenOneClickDeploy.name(),
				Tipo.KaizenOneClickDeploy, faseEsforcos);
		kaizenOneClick.setCusto(700);

		LOGGER.debug("Nova demanda Kaizen One Click Deploy criada.");

		return kaizenOneClick;
	}

	public static Demanda criarDemandaKaizenContinuousDelivery() {
		Map<FaseDemanda, Integer> faseEsforcos = criarFaseEsforcos(3, 3, 3);

		Demanda kaizenContinuous = new Demanda(
				Tipo.KaizenContinuousDelivery.name(),
				Tipo.KaizenContinuousDelivery, faseEsforcos);
		kaizenContinuous.setCusto(700);

		LOGGER.debug("Nova demanda Kaizen Continuous Delivery criada.");

		return kaizenContinuous;
	}

	private static Map<FaseDemanda, Integer> criarFaseEsforcos(int esforcoAN,
			int esforcoDev, int esforcoAQ) {
		Map<FaseDemanda, Integer> faseEsforcos = new HashMap<FaseDemanda, Integer>();
		faseEsforcos.put(FaseDemanda.Analysis, esforcoAN);
		faseEsforcos.put(FaseDemanda.Development, esforcoDev);
		faseEsforcos.put(FaseDemanda.Testing, esforcoAQ);
		faseEsforcos.put(FaseDemanda.DeployReady, 0);
		return faseEsforcos;
	}

}

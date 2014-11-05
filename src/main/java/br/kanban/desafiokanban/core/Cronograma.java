package br.kanban.desafiokanban.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cronograma {
	private static final Logger LOGGER = LogManager.getLogger(Cronograma.class);
	private static Integer dia = 0;

	public static void avancarDia() {
		++dia;
		LOGGER.info("Agora estamos no dia " + dia + ".");
	}

	public static Integer getDia() {
		return dia;
	}

}

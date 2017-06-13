package br.com.virtz.condominio.exception;

import br.com.virtz.condominio.entidades.Entidade;

public class ParametroObrigatorioNuloException extends AppException {

	private static final long serialVersionUID = 1L;


	public ParametroObrigatorioNuloException(String mensagem) {
		super(mensagem);
	}

	
}

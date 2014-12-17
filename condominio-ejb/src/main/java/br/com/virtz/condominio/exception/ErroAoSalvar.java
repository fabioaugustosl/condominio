package br.com.virtz.condominio.exception;

import br.com.virtz.condominio.entidades.Entidade;

public class ErroAoSalvar extends AppException {

	private static final long serialVersionUID = 1L;

	private Entidade entidade;

	public ErroAoSalvar(String mensagem, Entidade entidade) {
		super(mensagem);
		this.entidade = entidade;
	}

	public Entidade getEntidade() {
		return entidade;
	}
	
	
}

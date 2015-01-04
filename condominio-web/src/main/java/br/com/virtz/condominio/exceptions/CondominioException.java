package br.com.virtz.condominio.exceptions;

public class CondominioException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	protected String mensagem;

	public CondominioException(String mensagem) {
		super();
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}

}

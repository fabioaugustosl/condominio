package br.com.virtz.condominio.exceptions;

public class ErroConversaoException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	protected String mensagem;

	public ErroConversaoException(String mensagem) {
		super();
		this.mensagem = mensagem;
	}
	
	public String getMessage() {
		return mensagem;
	};

}

package br.com.virtz.condominio.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

/**
 * Exceção padrão/geral do sistema. Todas as demais exceções devem por
 * padrão extender essa classe.
 */
@ApplicationException(rollback=true)
public class AppException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean exibirDetalhes;
	
	
	/**
	 * Exceção padrão recebendo apenas uma mensagem.
	 * 
	 * @param mensagem
	 */
	public AppException(String mensagem) {
		super(mensagem);
	}
	
	/**
	 * Exceção padrão recebendo apenas uma mensagem.
	 * 
	 * @param mensagem
	 * @param exibirDetalhes  - habilita ou não a exibição do stacktrace na tela
	 */
	public AppException(String mensagem, boolean exibirDetalhes) {
		super(mensagem);
		this.exibirDetalhes = exibirDetalhes;
	}
	
	
	public boolean exibirDetalhes() {
		return this.exibirDetalhes;
	}
	
	@Override
	public String toString() {
		return this.getLocalizedMessage();
	}
	
}

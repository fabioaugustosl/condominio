package br.com.virtz.condominio.service;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Token;

@Local
public interface ITokenService {

	public boolean tokenEhValido(String token);
	public Token novoToken();
	public void invalidar(String token);
	
}

package br.com.virtz.condominio.service;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Local
public interface ITokenService {

	public boolean tokenEhValido(String token);
	public Token recuperarToken(String token);
	public Token novoToken()  throws ErroAoSalvar;
	public Token novoToken(String chaveEntidade)  throws ErroAoSalvar;
	public void invalidar(String token);
}

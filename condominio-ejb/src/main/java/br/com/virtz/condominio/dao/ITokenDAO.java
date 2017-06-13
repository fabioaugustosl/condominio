package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Token;

@Local
public interface ITokenDAO extends CrudDAO<Token> {
	public Token recuperar(String token);
	public void invalidarToken(String token);
}

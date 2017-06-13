package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Token;

@Stateless
public class TokenDAO extends DAO<Token> implements ITokenDAO {

	@Override
	public Token recuperar(String token) {
		Query qry = getEntityManager().createNamedQuery("Token.recuperarPorToken");
		qry.setParameter("token", token);
		List<Token> resultado = qry.getResultList();
		return resultado == null || resultado.isEmpty() ? null : resultado.get(0);
	}

	@Override
	public void invalidarToken(String token) {
		Query qry = getEntityManager().createQuery("UPDATE token SET ativo = 0 WHERE token = :token ");
		qry.setParameter("token", token);
		qry.executeUpdate();
	}
	
}

package br.com.virtz.condominio.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.geral.IGerarToken;

@Stateless
public class TokenService implements ITokenService {
	
	
	// REPOSITORY
	
	@EJB
	private IGerarToken gerarToken;
	

	@Override
	public boolean tokenEhValido(String token) {
		// TODO busca o token pela string
		Token t = new Token();
		if(t != null && t.getAtivo() 
				&& (t.getDataExpiracao() == null || t.getDataExpiracao().before(new Date()))){
			return true;
		}
		
		return false;
	}

	@Override
	public Token novoToken() {
		return gerarToken.gerar();
	}

	@Override
	public void invalidar(String token) {
		// repository dando update no token
	}

}

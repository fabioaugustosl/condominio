package br.com.virtz.condominio.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.ITokenDAO;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.geral.IGerarToken;

@Stateless
public class TokenService implements ITokenService {
	
	
	@EJB
	private ITokenDAO tokenDAO;
	
	@EJB
	private IGerarToken gerarToken;
	

	@Override
	public boolean tokenEhValido(String token) {
		Token t = tokenDAO.recuperar(token);
		if(t != null && t.getAtivo() 
				&& (t.getDataExpiracao() == null || t.getDataExpiracao().before(new Date()))){
			return true;
		}
		
		return false;
	}

	
	@Override
	public Token recuperarToken(String token) {
		return tokenDAO.recuperar(token);
	}
	
	
	@Override
	public Token novoToken() throws ErroAoSalvar {
		Token token = gerarToken.gerar();
		try {
			token = tokenDAO.salvar(token);
		} catch (Exception e) {
			throw new ErroAoSalvar("Ocorreu um erro ao gerar o token", null);
		}
		return token;
	}
	
	
	@Override
	public Token novoToken(String chaveEntidade)  throws ErroAoSalvar {
		Token token = gerarToken.gerar();
		try {
			token.setChaveEntidade(chaveEntidade);
			token = tokenDAO.salvar(token);
		} catch (Exception e) {
			throw new ErroAoSalvar("Ocorreu um erro ao gerar o token", null);
		}
		return token;
	}
	

	@Override
	public void invalidar(String token) {
		tokenDAO.invalidarToken(token);
	}


}

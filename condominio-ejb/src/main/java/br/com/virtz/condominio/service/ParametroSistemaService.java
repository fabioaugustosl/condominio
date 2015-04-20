package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.dao.IParametroSistemaDAO;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Stateless
public class ParametroSistemaService implements IParametroSistemaService {

	@EJB
	private IParametroSistemaDAO parametroDAO;

	@Override
	public List<ParametroSistema> recuperarTodos(Condominio condominio) {
		return parametroDAO.recuperarTodos();
	}

	@Override
	public ParametroSistema recuperar(EnumParametroSistema parametro, Condominio condominio) {
		return parametroDAO.recuperar(parametro, condominio);
	}

	@Override
	public ParametroSistema salvar(ParametroSistema parametro) throws ErroAoSalvar {
		try {
			return parametroDAO.salvar(parametro);
		} catch (Exception e) {
			throw new ErroAoSalvar("Aconteceu um erro ao atualizar o parâmetro "+ parametro.getNome(), parametro);
		}
	}

}

package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IParametroSistemaDAO;
import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.ParametroSistema;

@Stateless
public class ParametroSistemaService implements IParametroSistemaService {

	@EJB
	private IParametroSistemaDAO parametroDAO;

	@Override
	public List<ParametroSistema> recuperarTodos(Condominio condominio) {
		return parametroDAO.recuperarTodos();
	}

}

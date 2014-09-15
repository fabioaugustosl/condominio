package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.entity.Condominio;

@Stateless
public class CondominioService implements ICondominioService {

	@EJB
	private ICondominioDAO condominioDAO;

	@Override
	public List<Condominio> recuperarTodos() {
		return condominioDAO.recuperarTodos();
	}

	@Override
	public void remover(Long id) {
		condominioDAO.remover(id);
	}

	@Override
	public Condominio salvar(Condominio condominio) throws Exception {
		return condominioDAO.salvar(condominio);
	}

}

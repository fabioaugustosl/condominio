package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.ILembreteSindicoDAO;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.LembreteSindico;

@Stateless
public class LembreteSindicoService implements ILembreteSindicoService {

	@EJB
	private ILembreteSindicoDAO lembreteSindicoDAO;

	
	@Override
	public LembreteSindico salvar(LembreteSindico lembreteSindico) throws Exception {
		return lembreteSindicoDAO.salvar(lembreteSindico);
	}

	@Override
	public void remover(Long id) {
		lembreteSindicoDAO.remover(id);
	}

	@Override
	public List<LembreteSindico> recuperarTodos(Condominio condominio) {
		return lembreteSindicoDAO.recuperar(condominio.getId());
	}

	
}

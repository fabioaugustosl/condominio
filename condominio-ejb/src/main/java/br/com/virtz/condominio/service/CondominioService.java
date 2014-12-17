package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IAreaComumDAO;
import br.com.virtz.condominio.dao.IBlocoDAO;
import br.com.virtz.condominio.dao.ICondominioDAO;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class CondominioService implements ICondominioService {

	@EJB
	private ICondominioDAO condominioDAO;
	
	@EJB
	private IBlocoDAO blocoDAO;

	
	@EJB
	private IAreaComumDAO areaComumDAO;

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

	@Override
	public Bloco salvarBloco(Bloco bloco) throws Exception {
		return blocoDAO.salvar(bloco);
	}

	@Override
	public void removerBloco(Long id) {
		blocoDAO.remover(id);
	}

	@Override
	public List<Bloco> recuperarTodosBlocos() {
		return blocoDAO.recuperarTodos();
	}

	@Override
	public Condominio recuperarCondominioCompleto(Usuario usuario) {
		return condominioDAO.recuperarCondominioCompleto(usuario);
	}

	@Override
	public AreaComum salvarAreaComum(AreaComum area) throws Exception {
		return areaComumDAO.salvar(area);
	}

	@Override
	public void removerAreaComum(Long id) {
		areaComumDAO.remover(id);
	}


}

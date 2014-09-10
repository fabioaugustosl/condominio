package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IBlocoDAO;
import br.com.virtz.condominio.entity.Bloco;

// REgra de negócio 
@Stateless
public class BlocoService implements IBlocoService {

	@EJB
	private IBlocoDAO blocoDAO;

	@Override
	public List<Bloco> recuperarTodos() {
		return blocoDAO.recuperarTodos();
	}

	@Override
	public void remover(Long id) {
		blocoDAO.remover(id);
	}

	@Override
	public Bloco salvar(Bloco bloco) throws Exception {
		return blocoDAO.salvar(bloco);
	}

}

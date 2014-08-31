package br.com.virtz.condominio.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.virtz.condominio.dao.IBlocoDAO;
import br.com.virtz.condominio.entity.Bloco;

@Stateless
public class BlocoService implements IBlocoService {

   @EJB
   private IBlocoDAO blocoDAO;

	@Override
	public List<Bloco> recuperarTodos() {
		return blocoDAO.recuperarTodos();
	}

}

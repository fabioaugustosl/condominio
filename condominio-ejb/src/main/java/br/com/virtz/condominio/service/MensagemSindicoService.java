package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IMensagemSindicoDAO;
import br.com.virtz.condominio.entidades.MensagemSindico;

@Stateless
public class MensagemSindicoService implements IMensagemSindicoService {

	@EJB
	private IMensagemSindicoDAO mensagemDAO;

	@Override
	public MensagemSindico salvar(MensagemSindico mensagem) throws Exception {
		return mensagemDAO.salvar(mensagem);
	}

	@Override
	public void remover(Long id) {
		mensagemDAO.remover(id);
	}

	@Override
	public List<MensagemSindico> recuperarTodos(Long idCondominio) {
		return mensagemDAO.recuperar(idCondominio);
	}
	

	
}

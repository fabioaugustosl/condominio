package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IEmailEnviarDAO;
import br.com.virtz.condominio.entidades.EmailParaEnviar;

@Stateless
public class EmailEnviarService implements IEmailEnviarService {

	@EJB
	private IEmailEnviarDAO mensagemDAO;


	@Override
	public EmailParaEnviar salvar(EmailParaEnviar mensagem) throws Exception {
		if(mensagem == null){
			throw new Exception("Mensagem nula");
		}

		return mensagemDAO.salvar(mensagem);
	}

	@Override
	public void remover(Long id) {
		mensagemDAO.remover(id);
	}

	@Override
	public List<EmailParaEnviar> recuperarNaoEnviados() {
		return mensagemDAO.recuperarnaoEnviados();
	}

}

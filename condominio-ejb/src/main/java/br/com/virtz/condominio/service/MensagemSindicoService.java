package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IMensagemSindicoDAO;
import br.com.virtz.condominio.dao.IRespostaMensagemSindicoDAO;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;

@Stateless
public class MensagemSindicoService implements IMensagemSindicoService {

	@EJB
	private IMensagemSindicoDAO mensagemDAO;
	
	@EJB
	private IRespostaMensagemSindicoDAO respostaDAO;
	
	

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

	@Override
	public MensagemSindico recuperarMensagemSindico(Long id) {
		return mensagemDAO.recuperarPorId(id);
	}
	
	
	@Override
	public RespostaMensagemSindico salvar(RespostaMensagemSindico resposta) throws Exception {
		return respostaDAO.salvar(resposta);
	}

	@Override
	public RespostaMensagemSindico recuperarUltimaResposta(Long idMensagem) {
		List<RespostaMensagemSindico> respostas = respostaDAO.recuperar(idMensagem);
		if(respostas != null && !respostas.isEmpty()){
			return respostas.get(0);
		}
		return  null;
	}

}

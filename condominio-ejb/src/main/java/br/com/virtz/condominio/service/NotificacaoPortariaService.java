package br.com.virtz.condominio.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.INotificacaoPortariaDAO;
import br.com.virtz.condominio.entidades.NotificacaoPortaria;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class NotificacaoPortariaService implements INotificacaoPortariaService {

	@EJB
	private INotificacaoPortariaDAO notificacaoDAO;



	@Override
	public void remover(Long idNotificacao) throws AppException {
		notificacaoDAO.remover(idNotificacao);
	}


	@Override
	public NotificacaoPortaria recuperar(Long idNotificacao) throws AppException {
		if(idNotificacao == null){
			throw new AppException("A notificacao é obrigatória.");
		}
		return notificacaoDAO.recuperarPorId(idNotificacao);
	}


	@Override
	public void salvar(NotificacaoPortaria notificao) throws AppException {
		if(notificao == null){
			throw new AppException("A notificação é obrigatória.");
		}

		try {
			notificacaoDAO.salvar(notificao);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar notificação");
		}
	}


	@Override
	public List<NotificacaoPortaria> recuperarPorCondominio(Long idCondominio) {
		return notificacaoDAO.recuperarPorCondominio(idCondominio);
	}
	
	
	@Override
	public List<NotificacaoPortaria> recuperarUltimasNotificacoes(Long idCondominio) {
		return notificacaoDAO.recuperarUltimasNotificacoesConfirmadas(idCondominio, 5);
	}


	@Override
	public void confirmar(Long idNotificacao) throws AppException {
		NotificacaoPortaria n = notificacaoDAO.recuperarPorId(idNotificacao);
		
		if(n != null){
			n.setDataConfirmacao(new Date());
			try {
				notificacaoDAO.salvar(n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}


}

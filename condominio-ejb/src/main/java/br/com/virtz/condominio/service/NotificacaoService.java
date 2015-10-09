package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoNotificacao;
import br.com.virtz.condominio.dao.INotificacaoDAO;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Notificacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class NotificacaoService implements INotificacaoService {

	@EJB
	private INotificacaoDAO notificacaoDAO;
	
	
	
	@Override
	public List<Notificacao> recuperar(Long idCondominio, Long idUsuario) {
		if(idCondominio == null || idUsuario == null){
			return new ArrayList<Notificacao>();
		}
		return notificacaoDAO.recuperar(idCondominio, idUsuario);
	}


	@Override
	public void salvarNotificacao(Notificacao notificacao) throws AppException {
		try {
			notificacaoDAO.salvar(notificacao);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar notificação.");
		}
	}


	@Override
	public void remover(Long idNotificacao) throws AppException {
		if(idNotificacao == null ){
			throw new AppException("Erro na identificação da notificação para exclusão.");
		}
		
		try{
			this.notificacaoDAO.remover(idNotificacao);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro ao remover a notificação.");
		}
	}


	@Override
	public void salvarNovaNotificacao(Condominio condominio, Usuario usuario, EnumTipoNotificacao tipo, String texto) throws AppException {
		if(condominio == null ){
			throw new AppException("É obrigatório informar o condominio para salvar.");
		}
		
		if(usuario == null ){
			throw new AppException("É obrigatório informar o usuario para salvar.");
		}
		
		try {
			if(tipo == null){
				tipo = EnumTipoNotificacao.AVISO;
			}
			Notificacao n = new Notificacao();
			n.setCondominio(condominio);
			n.setUsuario(usuario);
			n.setTexto(texto);
			n.setData(new Date());
			n.setTipoNotificacao(tipo);
			
			notificacaoDAO.salvar(n);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar notificação.");
		}
	}
	

}

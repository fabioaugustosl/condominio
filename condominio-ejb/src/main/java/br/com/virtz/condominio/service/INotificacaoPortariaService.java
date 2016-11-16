package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.NotificacaoPortaria;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface INotificacaoPortariaService  {

	public List<NotificacaoPortaria> recuperarPorCondominio(Long idCondominio);
	public NotificacaoPortaria recuperar(Long idNotificacao) throws AppException;
	public void salvar(NotificacaoPortaria notificaao) throws AppException ;
	public void remover(Long idNotificacao) throws AppException;

}


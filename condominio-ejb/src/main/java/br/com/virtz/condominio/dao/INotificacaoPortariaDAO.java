package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.NotificacaoPortaria;

@Local
public interface INotificacaoPortariaDAO extends CrudDAO<NotificacaoPortaria> {

	public List<NotificacaoPortaria> recuperarPorCondominio(Long idCondominio);
	public List<NotificacaoPortaria> recuperarUltimasNotificacoesConfirmadas(Long idCondominio, Integer quantidade);

}

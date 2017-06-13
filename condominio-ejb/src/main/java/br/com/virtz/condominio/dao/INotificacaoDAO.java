package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Notificacao;

@Local
public interface INotificacaoDAO extends CrudDAO<Notificacao> {
	public List<Notificacao> recuperar(Long idCondominio, Long idUsuario);
}

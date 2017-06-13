package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.RespostaMensagemSindico;

@Local
public interface IRespostaMensagemSindicoDAO extends CrudDAO<RespostaMensagemSindico> {
	public List<RespostaMensagemSindico> recuperar(Long idMensagem);
}

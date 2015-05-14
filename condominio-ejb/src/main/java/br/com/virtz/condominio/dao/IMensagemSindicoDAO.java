package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.MensagemSindico;

@Local
public interface IMensagemSindicoDAO extends CrudDAO<MensagemSindico> {
	public List<MensagemSindico> recuperar(Long idCondominio);
}

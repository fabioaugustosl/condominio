package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.OpcaoVotacao;

@Local
public interface IOpcaoVotacaoDAO extends CrudDAO<OpcaoVotacao> {

	public OpcaoVotacao rcuperarPorId(Long id);

}

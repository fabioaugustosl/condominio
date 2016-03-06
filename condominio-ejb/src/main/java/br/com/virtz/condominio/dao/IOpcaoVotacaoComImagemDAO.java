package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.OpcaoVotacaoComImagem;

@Local
public interface IOpcaoVotacaoComImagemDAO extends CrudDAO<OpcaoVotacaoComImagem> {

	public OpcaoVotacaoComImagem recuperarPorId(Long id);

}

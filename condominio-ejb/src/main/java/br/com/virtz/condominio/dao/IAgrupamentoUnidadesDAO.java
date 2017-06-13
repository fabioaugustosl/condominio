package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AgrupamentoUnidades;

@Local
public interface IAgrupamentoUnidadesDAO extends CrudDAO<AgrupamentoUnidades> {
	public List<AgrupamentoUnidades> recuperarComBlocos(Long idCondominio);
	public boolean condominioPossuiAgrupamento(Long idCondominio);
}

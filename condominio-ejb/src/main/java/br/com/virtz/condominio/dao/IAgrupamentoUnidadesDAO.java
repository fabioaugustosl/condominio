package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AgrupamentoUnidades;
import br.com.virtz.condominio.entidades.Bloco;

@Local
public interface IAgrupamentoUnidadesDAO extends CrudDAO<AgrupamentoUnidades> {
	public List<AgrupamentoUnidades> recuperarComBlocos(Long idCondominio);
}

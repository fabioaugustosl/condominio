package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Visitante;

@Local
public interface IVisitanteDAO extends CrudDAO<Visitante> {
	public List<Visitante> recuperar(Long idCondominio);
}

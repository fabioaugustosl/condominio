package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Assembleia;

@Local
public interface IAssembleiaDAO extends CrudDAO<Assembleia> {
	public List<Assembleia> recuperar(Long idCondominio);
	public List<Assembleia> recuperarNaoRealizadas(Long idCondominio);
	public Assembleia recuperarUltimaAssembleia(Long idCondominio);
}

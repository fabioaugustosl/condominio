package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Visitante;

@Local
public interface IVisitanteDAO extends CrudDAO<Visitante> {
	public List<Visitante> recuperar(Long idCondominio);
	public int totalVisitantes(Long idCondominio);
	public List<Visitante> recuperarPaginado(Long idCondominio, int inicio, int qtdRegistros);
	
	public int totalVisitantesApartamento(Long idApartamento);
	public List<Visitante> recuperarPaginadoApartamento(Long idApartamento, int inicio, int qtdRegistros);
}

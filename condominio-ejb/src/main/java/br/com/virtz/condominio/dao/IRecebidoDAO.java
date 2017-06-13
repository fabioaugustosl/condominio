package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Recebido;

@Local
public interface IRecebidoDAO extends CrudDAO<Recebido> {
	public List<Recebido> recuperarPorApartamento(Long idApartamento);
	public List<Recebido> recuperarPorCondominio(Long idCondominio);
	
	// listagem paginado
	public List<Recebido> recuperarPaginado(Long idCondominio, int inicio, int qtdRegistros);
	public int totalRecebidos(Long idCondominio);
	
	public List<Recebido> recuperarPaginadoApto(Long idApartamento, int inicio, int qtdRegistros);
	public int totalRecebidosApto(Long idApartamento);
	
	
}

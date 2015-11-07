package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.CobrancaUsuario;

@Local
public interface ICobrancaUsuarioDAO extends CrudDAO<CobrancaUsuario> {
	public List<CobrancaUsuario> recuperarPorCondominio(Long idCondominio);
	public List<CobrancaUsuario> recuperarPorCondominio(Long idCondominio, Integer ano, Integer mes);
	public CobrancaUsuario recuperar(Long idCondominio, Long idUsuario, Integer ano, Integer mes);
	public List<CobrancaUsuario> recuperarCobrancasDeAvulsosPorCondominio(Long idCondominio, Integer ano, Integer mes);
	
	/**
	 * Retorna os anos e meses que foram geradas as cobranças.
	 * 
	 * @param idCondominio
	 * @return  List<Object[]> onde [0] = ano , [1] = mes
	 */
	public List<Object[]> recuperarAnosMeses(Long idCondominio);
}

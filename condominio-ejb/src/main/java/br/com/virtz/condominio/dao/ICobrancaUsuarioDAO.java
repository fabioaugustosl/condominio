package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.CobrancaUsuario;

@Local
public interface ICobrancaUsuarioDAO extends CrudDAO<CobrancaUsuario> {
	public List<CobrancaUsuario> recuperarPorCondominio(Long idCondominio);
	public List<CobrancaUsuario> recuperarPorCondominio(Long idCondominio, Integer ano, Integer mes);
	public CobrancaUsuario recuperar(Long idCondominio, Long idUsuario, Integer ano, Integer mes);
}

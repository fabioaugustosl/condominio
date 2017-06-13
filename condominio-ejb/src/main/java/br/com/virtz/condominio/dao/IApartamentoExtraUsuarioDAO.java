package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ApartamentoExtraUsuario;

@Local
public interface IApartamentoExtraUsuarioDAO extends CrudDAO<ApartamentoExtraUsuario> {
	public List<ApartamentoExtraUsuario> recuperar(Long idUsuario);
	public void removerTodos(Long idUsuario);
}

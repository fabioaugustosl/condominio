package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Estado;

@Local
public interface IEstadoDAO  {
	
	public List<Estado> recuperarTodos();
	
}

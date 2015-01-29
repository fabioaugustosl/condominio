package br.com.virtz.condominio.dao;

import java.util.List;

public interface CrudDAO<T> {
	public T recuperarPorId(Long id);
	public void remover(Long id);
	public T salvar(T t) throws Exception;
	public List<T> recuperarTodos();
}

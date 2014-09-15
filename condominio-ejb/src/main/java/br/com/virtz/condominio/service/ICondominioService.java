package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Condominio;

@Local
public interface ICondominioService {
	
	public Condominio salvar(Condominio condominio) throws Exception;
	public void remover(Long id);
	public List<Condominio> recuperarTodos();
	
}

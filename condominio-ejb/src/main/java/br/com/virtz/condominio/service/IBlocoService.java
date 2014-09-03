package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Bloco;

@Local
public interface IBlocoService {
	
	public Bloco salvar(Bloco bloco) throws Exception;
	public void remover(Long id);
	public List<Bloco> recuperarTodos();
	
}

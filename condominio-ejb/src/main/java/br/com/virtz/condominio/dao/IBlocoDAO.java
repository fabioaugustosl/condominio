package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Bloco;

@Local
public interface IBlocoDAO {
	public List<Bloco> recuperarTodos();
}

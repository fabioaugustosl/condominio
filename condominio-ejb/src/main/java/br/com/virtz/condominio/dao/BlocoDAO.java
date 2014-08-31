package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;

import br.com.virtz.condominio.entity.Bloco;

@Stateless
public class BlocoDAO extends DAO implements IBlocoDAO  {

	@Override
	public List<Bloco> recuperarTodos() {
		return null;
	} 
	
	
}

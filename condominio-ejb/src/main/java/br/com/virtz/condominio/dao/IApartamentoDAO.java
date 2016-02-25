package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Apartamento;

@Local
public interface IApartamentoDAO extends CrudDAO<Apartamento> {
	
	public Apartamento recuperarPorNumero(Long idCondominio, String numeroApto, String nomeBloco);
	
}

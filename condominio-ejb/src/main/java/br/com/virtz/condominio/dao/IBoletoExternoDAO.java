package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.BoletoExterno;

@Local
public interface IBoletoExternoDAO extends CrudDAO<BoletoExterno> {
	
	public BoletoExterno recuperar(Long idCondominio);
	
}

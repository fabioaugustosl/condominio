package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.LembreteSindico;

@Local
public interface ILembreteSindicoDAO extends CrudDAO<LembreteSindico> {
	public List<LembreteSindico> recuperar(Long idCondominio);
}

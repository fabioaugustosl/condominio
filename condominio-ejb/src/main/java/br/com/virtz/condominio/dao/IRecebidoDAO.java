package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Recebido;

@Local
public interface IRecebidoDAO extends CrudDAO<Recebido> {
	public List<Recebido> recuperarPorApartamento(Long idApartamento);
	public List<Recebido> recuperarPorCondominio(Long idCondominio);
}

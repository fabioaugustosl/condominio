package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Apartamento;

@Local
public interface IApartamentoDAO extends CrudDAO<Apartamento> {

	public Apartamento recuperarPorNumero(Long idCondominio, String numeroApto, String nomeBloco);
	public Apartamento recuperarPorNumero(Long idCondominio, String numeroApto, String nomeBloco, String nomeAgrupamento);
	public List<Apartamento> recuperarAptosNaoAssociados(Long idCondominio);

}

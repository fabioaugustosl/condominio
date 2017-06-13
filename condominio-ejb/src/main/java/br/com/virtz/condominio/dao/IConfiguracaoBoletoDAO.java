package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ConfiguracaoBoleto;

@Local
public interface IConfiguracaoBoletoDAO extends CrudDAO<ConfiguracaoBoleto> {
	public List<ConfiguracaoBoleto> recuperarPorCondominio(Long idCondominio);
	public ConfiguracaoBoleto recuperarPorCondominio(Long idCondominio, Integer ano, Integer mes);
}

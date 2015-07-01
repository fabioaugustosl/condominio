package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Indicacao;

@Local
public interface IIndicacaoDAO extends CrudDAO<Indicacao> {
	public List<Indicacao> recuperar(Long idCondominio);
	public List<Indicacao> recuperar(Long idCondominio, int limite);
	public List<Indicacao> recuperarPorCategoria(Long idCondominio, Long idCategoria);
}

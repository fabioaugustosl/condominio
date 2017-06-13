package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Noticia;

@Local
public interface INoticiaDAO extends CrudDAO<Noticia> {
	public List<Noticia> recuperar(Long idCondominio);
	public List<Noticia> recuperarAtivas(Long idCondominio, Integer totalRegistros);
}

package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Documento;

@Local
public interface IDocumentoDAO extends CrudDAO<Documento> {
	public List<Documento> recuperar(Condominio condominio);
}

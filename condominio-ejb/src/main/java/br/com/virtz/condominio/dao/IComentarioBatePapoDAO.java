package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.ComentarioBatePapo;
import br.com.virtz.condominio.entidades.Condominio;

@Local
public interface IComentarioBatePapoDAO extends CrudDAO<ComentarioBatePapo> {
	public List<ComentarioBatePapo> recuperar(BatePapo batePapo);
}

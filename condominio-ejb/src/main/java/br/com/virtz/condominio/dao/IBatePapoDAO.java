package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.Condominio;

@Local
public interface IBatePapoDAO extends CrudDAO<BatePapo> {
	public List<BatePapo> recuperar(Condominio condominio);
}

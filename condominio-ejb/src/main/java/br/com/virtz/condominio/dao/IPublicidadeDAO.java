package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.constantes.EnumTipoPublicidade;
import br.com.virtz.condominio.entidades.Publicidade;

@Local
public interface IPublicidadeDAO extends CrudDAO<Publicidade> {
	public Publicidade recuperar(Long idCondominio, EnumTipoPublicidade tipo);
}

package br.com.virtz.condominio.dao;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entity.ParametroSistema;

@Local
public interface IParametroSistemaDAO extends CrudDAO<ParametroSistema> {
	
	public ParametroSistema recuperar(EnumParametroSistema parametro);
}

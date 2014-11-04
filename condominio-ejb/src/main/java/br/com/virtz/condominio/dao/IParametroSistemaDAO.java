package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.ParametroSistema;

@Local
public interface IParametroSistemaDAO extends CrudDAO<ParametroSistema> {
	
	public ParametroSistema recuperar(EnumParametroSistema parametro, Condominio condominio);
	public List<ParametroSistema> recuperarTodos(Condominio condominio);
	
}

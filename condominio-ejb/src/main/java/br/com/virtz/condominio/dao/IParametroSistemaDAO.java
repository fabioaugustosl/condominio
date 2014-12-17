package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ParametroSistema;

@Local
public interface IParametroSistemaDAO extends CrudDAO<ParametroSistema> {
	
	public ParametroSistema recuperar(EnumParametroSistema parametro, Condominio condominio);
	public List<ParametroSistema> recuperarTodos(Condominio condominio);
	
}

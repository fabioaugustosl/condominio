package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Local
public interface IParametroSistemaService {
	public List<ParametroSistema> recuperarTodos(Condominio condominio);
	public ParametroSistema recuperar(EnumParametroSistema parametro, Condominio condominio);
	public ParametroSistema salvar(ParametroSistema parametro)  throws ErroAoSalvar ;
}

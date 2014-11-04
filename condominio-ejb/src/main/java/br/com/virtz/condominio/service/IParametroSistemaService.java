package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.AreaComum;
import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.ParametroSistema;
import br.com.virtz.condominio.entity.Usuario;

@Local
public interface IParametroSistemaService {
	public List<ParametroSistema> recuperarTodos(Condominio condominio);
}

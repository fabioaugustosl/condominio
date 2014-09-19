package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.AreaComum;
import br.com.virtz.condominio.entity.Bloco;
import br.com.virtz.condominio.entity.Condominio;
import br.com.virtz.condominio.entity.Usuario;

@Local
public interface ICondominioService {
	// condominio
	public Condominio salvar(Condominio condominio) throws Exception;
	public void remover(Long id);
	public List<Condominio> recuperarTodos();
	public Condominio recuperarCondominioCompleto(Usuario usuario);
	
	// bloco
	public Bloco salvarBloco(Bloco bloco) throws Exception;
	public void removerBloco(Long id);
	public List<Bloco> recuperarTodosBlocos();
	
	// areas comuns
	public AreaComum salvarAreaComum(AreaComum area) throws Exception;
	public void removerAreaComum(Long id);
}

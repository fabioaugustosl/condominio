package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entity.Usuario;

@Local
public interface IUsuarioService {
	
	public Usuario salvar(Usuario bloco) throws Exception;
	public void remover(Long id);
	public List<Usuario> recuperarTodos();
	
}

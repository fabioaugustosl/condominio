package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;

@Local
public interface IUsuarioService {
	
	public Usuario salvar(Usuario bloco) throws Exception;
	public void remover(Long id);
	public List<Usuario> recuperarTodos();
	public Usuario recuperarUsuarioCompleto(Long id);
	public List<Usuario> recuperarTodos(Condominio condominio);
	
	public void alterarParaSindico(Long idUsuario);
	public void alterarParaMorador(Long idUsuario);
	public void alterarParaAdministrativo(Long idUsuario);
	
}

package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IUsuarioService {
	
	public Usuario salvar(Usuario usuario) throws AppException ;
	public Usuario salvarNovo(Usuario usuario) throws AppException ;
	public ArquivoUsuario salvarArquivo(ArquivoUsuario arquivo) throws AppException ;
	public void remover(Long id);
	public List<Usuario> recuperarTodos();
	public Usuario recuperarUsuarioCompleto(Long id);
	public List<Usuario> recuperarTodos(Condominio condominio);
	
	public void alterarParaSindico(Long idUsuario);
	public void alterarParaMorador(Long idUsuario);
	public void alterarParaAdministrativo(Long idUsuario);
	
}

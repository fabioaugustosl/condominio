package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.entidades.ApartamentoExtraUsuario;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;
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
	public List<Usuario> recuperarSindicos(Long idCondominio);
	public Usuario recuperarUsuarioCompleto(Long id);
	public List<Usuario> recuperarTodos(Condominio condominio);

	public void alterarParaSindico(Long idUsuario);
	public void alterarParaMorador(Long idUsuario);
	public void alterarParaAdministrativo(Long idUsuario);
	public String criptografarSenhaUsuario(String senha)throws AppException;
	public Usuario recuperarUsuario(String email);
	public List<Usuario> recuperarUsuariosPorEmail(String email);
	public List<Usuario> recuperarUsuariosPorApartamento(Long idApartamento);
	public boolean emailJaEstaAtivo(String email);
	public List<Usuario> recuperarTodosPorteiros(Long idCondominio);
	public List<Usuario> recuperarTodosAdministradores(Long idCondominio);

	public List<ApartamentoExtraUsuario> recuperarApartamentosExtras(Long idUsuario);
	public List<ApartamentoExtraUsuario> salvarApartamentosExtras(Long idUsuario, List<ApartamentoExtraUsuario> apartamentos);
	
	
	//Bloqueio de função
	public void bloquearFuncao(Usuario usuario, EnumFuncaoBloqueio funcao, String comentario) throws Exception;
	public void removerBloqueio(Long idUsuario, EnumFuncaoBloqueio funcao);
	public List<BloqueioFuncaoUsuario> recuperarBloqueios(Long idUsuario);
	public List<BloqueioFuncaoUsuario> recuperarBloqueios(Long idUsuario, EnumFuncaoBloqueio funcao);
	public List<BloqueioFuncaoUsuario> recuperarBloqueiosPorCondominio(Long idCondominio, EnumFuncaoBloqueio funcao);


}

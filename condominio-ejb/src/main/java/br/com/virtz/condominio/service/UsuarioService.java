package br.com.virtz.condominio.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.dao.IArquivoUsuarioDAO;
import br.com.virtz.condominio.dao.IBloqueioFuncaoUsuarioDAO;
import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.CriptografarSenha;

@Stateless
public class UsuarioService implements IUsuarioService {

	@EJB
	private IUsuarioDAO usuarioDAO;

	@EJB
	private IBloqueioFuncaoUsuarioDAO bloqueioDAO;

	@EJB
	private IArquivoUsuarioDAO arquivoUsuarioDAO;



	@Override
	public List<Usuario> recuperarTodos() {
		return usuarioDAO.recuperarTodos();
	}


	@Override
	public List<Usuario> recuperarTodos(Condominio condominio) {
		if(condominio == null){
			return null;
		}
		return usuarioDAO.recuperarTodos(condominio.getId());
	}


	@Override
	public List<Usuario> recuperarTodosPorteiros(Long idCondominio) {
		List<Usuario> usuarios = usuarioDAO.recuperarTodosAdm(idCondominio);
		if(usuarios == null){
			return null;
		}
		List<Usuario> porteiros = new ArrayList<Usuario>();
		for(Usuario u : usuarios){
			if(!u.getUnidade().getAdm()){
				porteiros.add(u);
			}
		}
		return porteiros;
	}


	@Override
	public List<Usuario> recuperarTodosAdministradores(Long idCondominio) {
		List<Usuario> usuarios = usuarioDAO.recuperarTodosAdm(idCondominio);
		if(usuarios == null){
			return null;
		}
		List<Usuario> administradores = new ArrayList<Usuario>();
		for(Usuario u : usuarios){
			if(u.getUnidade().getAdm()){
				administradores.add(u);
			}
		}
		return administradores;
	}


	@Override
	public void remover(Long id) {
		usuarioDAO.remover(id);
	}

	@Override
	public Usuario salvar(Usuario usuario) throws AppException {

		if(StringUtils.isNotBlank(usuario.getSenhaDigitada())){
			CriptografarSenha criptografar = new CriptografarSenha();
			String senha = null;
			senha = criptografarSenhaUsuario(usuario.getSenhaDigitada());
			usuario.setSenha(senha);
		}

		try {
			return usuarioDAO.salvar(usuario);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o usuario.");
		}
	}


	@Override
	public Usuario salvarNovo(Usuario usuario) throws AppException {

		if(StringUtils.isNotBlank(usuario.getSenhaDigitada()) && !usuario.getSenhaDigitada().equals(usuario.getSenhaDigitadaConfirmacao())){
			throw new AppException("Favor digitar a senhas iguais.");
		}

		Usuario usuarioEmail = usuarioDAO.recuperarUsuarioPorEmail(usuario.getEmail());
		if(usuarioEmail != null){
			// se o usuario for novo verifica se o
			if(usuario.getId() == null){
				String senhaCriptografada = criptografarSenhaUsuario(usuario.getSenhaDigitada());
				usuario.setSenha(senhaCriptografada);

				// se a senha for a mesma deixa passar. Ou seja, considera que eh o mesmo usuario tentando terminar o cadastro.
				if(senhaCriptografada != null && senhaCriptografada.equals(usuarioEmail.getSenha())){
					usuarioEmail.setNome(usuario.getNome());
					usuarioEmail.setCelular(usuario.getCelular());
					usuarioEmail.setTelefone(usuario.getTelefone());
					usuario = usuarioEmail;
				} else {
					// se a senha não for a mesma considera que é outra pessoa mesmo tentando cadastrar com o mesmo email.
					throw new AppException("Email já cadastrado no sistema.");
				}
			}

			// se o email é igual mas o id dos usuarios for diferente tem que barrar!
			if(!usuarioEmail.getId().equals(usuario.getId())){
				throw new AppException("Email já cadastrado no sistema.");
			}
		}

		if(usuario.getDataCadastro() == null){
			usuario.setDataCadastro(new Date());
		}

		usuario.setDeletado(Boolean.FALSE);

		if(usuario.getTipoUsuario() == null){
			usuario.setTipoUsuario(EnumTipoUsuario.MORADOR);
		}

		return salvar(usuario);
	}


	public String criptografarSenhaUsuario(String senha)throws AppException {
		CriptografarSenha criptografar = new CriptografarSenha();
		String senhaCriptografada = null;
		try {
			senhaCriptografada = criptografar.hash256(senha);
		} catch (NoSuchAlgorithmException e) {
			throw new AppException("Ocorreu um erro ao validar a senha do usuário.");
		}
		return senhaCriptografada;
	}


	@Override
	public Usuario recuperarUsuarioCompleto(Long id) {
		return usuarioDAO.recuperarUsuarioCompleto(id);
	}

	@Override
	public Usuario recuperarUsuario(String email) {
		return usuarioDAO.recuperarUsuarioPorEmail(email);
	}

	@Override
	public void alterarParaSindico(Long idUsuario){
		usuarioDAO.alterarStatus(idUsuario, EnumTipoUsuario.SINDICO);
	}

	@Override
	public void alterarParaMorador(Long idUsuario) {
		usuarioDAO.alterarStatus(idUsuario, EnumTipoUsuario.MORADOR);
	}

	@Override
	public void alterarParaAdministrativo(Long idUsuario) {
		usuarioDAO.alterarStatus(idUsuario, EnumTipoUsuario.ADMINISTRATIVO);
	}

	@Override
	public ArquivoUsuario salvarArquivo(ArquivoUsuario arquivo) throws AppException {
		try {
			return arquivoUsuarioDAO.salvar(arquivo);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a foto do usuário.");
		}
	}


	@Override
	public List<Usuario> recuperarSindicos(Long idCondominio) {
		return usuarioDAO.recuperarSindicos(idCondominio);
	}


	@Override
	public List<Usuario> recuperarUsuariosPorEmail(String email) {
		return usuarioDAO.recuperarUsuariosPorEmail(email);
	}


	@Override
	public boolean emailJaEstaAtivo(String email) {
		if(StringUtils.isBlank(email)){
			return false;
		}

		List<Usuario> usuarios = usuarioDAO.recuperarUsuariosPorEmail(email);
		if(usuarios != null && !usuarios.isEmpty()){
			for(Usuario u : usuarios){
				if(u.getCadastroConfirmado() && u.getCondominio() != null){
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public List<Usuario> recuperarUsuariosPorApartamento(Long idApartamento) {
		return usuarioDAO.recuperarUsuariosPorApartamento(idApartamento);
	}


	@Override
	public void bloquearFuncao(Usuario usuario, EnumFuncaoBloqueio funcao, String comentario) throws Exception {
		BloqueioFuncaoUsuario bloqueio = new BloqueioFuncaoUsuario();
		bloqueio.setComentario(comentario);
		bloqueio.setFuncaoBloqueio(funcao);
		bloqueio.setUsuario(usuario);

		bloqueioDAO.salvar(bloqueio);
	}


	@Override
	public void removerBloqueio(Long idUsuario, EnumFuncaoBloqueio funcao) {
		List<BloqueioFuncaoUsuario> bloqueio = bloqueioDAO.recuperarPorUsuarioEFuncao(idUsuario, funcao);
		if(bloqueio != null && !bloqueio.isEmpty()){
			bloqueioDAO.remover((bloqueio.get(0)).getId());
		}
	}


	@Override
	public List<BloqueioFuncaoUsuario> recuperarBloqueios(Long idUsuario) {
		List<BloqueioFuncaoUsuario> bloqueios = bloqueioDAO.recuperarPorUsuario(idUsuario);
		return bloqueios;
	}


	@Override
	public List<BloqueioFuncaoUsuario> recuperarBloqueios(Long idUsuario, EnumFuncaoBloqueio funcao) {
		return bloqueioDAO.recuperarPorUsuarioEFuncao(idUsuario, funcao);
	}


	@Override
	public List<BloqueioFuncaoUsuario> recuperarBloqueiosPorCondominio(Long idCondominio, EnumFuncaoBloqueio funcao) {
		return bloqueioDAO.recuperarPorCondominioEFuncao(idCondominio, funcao);
	}




}

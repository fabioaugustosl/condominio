package br.com.virtz.condominio.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.dao.IArquivoUsuarioDAO;
import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.CriptografarSenha;

@Stateless
public class UsuarioService implements IUsuarioService {

	@EJB
	private IUsuarioDAO usuarioDAO;
	
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

}

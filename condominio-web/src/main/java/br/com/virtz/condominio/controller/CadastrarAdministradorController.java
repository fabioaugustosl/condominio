package br.com.virtz.condominio.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Unidade;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarAdministradorController implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private ICondominioService condominioService;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper message;

	@Inject
	private NavigationPage navegacao;


	private Usuario usuario = null;
	private Usuario usuarioLogado = null;


	@PostConstruct
	public void init(){
		usuario = new Usuario();
		usuario.setCadastroConfirmado(Boolean.TRUE);
		usuario.setTipoUsuario(EnumTipoUsuario.ADMINISTRADOR);

		usuarioLogado = sessao.getUsuarioLogado();
	}


	public void salvar() throws AppException {
		if(usuario.getSenhaDigitada().length() < 6){
			throw new AppException("A senha deve possuir no mínimo 6 caracteres.");
		}

		// validar se o email já existe
		Usuario u = usuarioService.recuperarUsuario(usuario.getEmail());
		if(u != null){
			throw new AppException("O email digitado já está sendo utilizado por outro usuário. Favor escolha outro email.");
		}
		usuario.setDeletado(false);
		usuario.setCondominio(usuarioLogado.getCondominio());
		usuario.setAdm(true);
		List<Unidade> unidades = condominioService.recuperarTodasUnidades(usuarioLogado.getCondominio().getId());
		if(unidades != null && !unidades.isEmpty()){
			Unidade unidade = null;
			for(Unidade uni : unidades){
				if(uni.getAdm()){
					unidade = uni;
					break;
				}
			}
			usuario.setUnidade(unidade);
		}
        try {
        	usuario = usuarioService.salvar(usuario);
        	message.addInfo("Usuário administrador salvo com succeso.");
        } catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		}
	}


	@SuppressWarnings("static-access")
	public void irParaListagem(){
		navegacao.redirectToPage("/usuario/listagemUsuarioAdministrador.faces");
	}



	/*  GETTERS e SETTERs	 */

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}

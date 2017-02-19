package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entidades.AgrupamentoUnidades;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfirmarUsuarioCondominioController {

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private ICondominioService condominioService;

	@EJB
	private ITokenService tokenService;

	@Inject
	private NavigationPage navegacao;

	@Inject
	private EnviarEmailSuporteController emailSuporte;


	private boolean usuarioConfirmado;
	private boolean cadastrouApto;
	private Usuario usuario = null;
	private Boolean condominioPossuiAgrupamento;
	private List<AgrupamentoUnidades> agrupamentos = null;
	private AgrupamentoUnidades agrupamentoSelecionado = null;
	private List<Bloco> blocos = null;
	private Bloco blocoSelecionado = null;
	private Apartamento apartamentoSelecionado = null;

	private String token = null;


	@PostConstruct
	public void init(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		token = request.getParameter("token");

		// verifica se o token é valido
		if(tokenService.tokenEhValido(token)){
			Token tokenEntidade = tokenService.recuperarToken(token);

			// recuperar o id do usuario para busca-lo no banco
			String chave = tokenEntidade.getChaveEntidade();
			if(StringUtils.isNotBlank(chave)){
				usuario = usuarioService.recuperarUsuarioCompleto(Long.valueOf(chave));

				// validar se o email já está sendo usado e ativo
				if(usuarioService.emailJaEstaAtivo(usuario.getEmail())){
					usuarioConfirmado = Boolean.FALSE;
				} else {
					usuarioConfirmado = Boolean.TRUE;
				}
			}

			if(condominioPossuiAgrupamento()){
				agrupamentos = condominioService.recuperarTodosAgrupamentos(usuario.getCondominio().getId());
			} else {
				blocos = condominioService.recuperarTodosBlocosComApartamentos(usuario.getCondominio().getId());
			}

		} else {
			usuarioConfirmado = Boolean.FALSE;
		}

	}

	public void irParaLogin(){
		navegacao.redirectToPage("/login.faces");
	}

	public void salvar() throws AppException{
		if(StringUtils.isBlank(usuario.getSenha()) ||
				StringUtils.isBlank(usuario.getSenhaDigitada()) ||
				this.apartamentoSelecionado == null){
			throw new AppException("Todos os campos do formulário são obrigatórios.");
		}

		if(usuario.getSenhaDigitada().length() < 6){
			throw new AppException("A senha deve possuir no mínimo 6 caracteres.");
		}

		if(!usuario.getSenhaDigitada().equals(usuario.getSenhaDigitadaConfirmacao()) ){
			throw new AppException("A senha deve ser repetida corretamente.");
		}

		usuario.setCadastroConfirmado(Boolean.TRUE);
		usuario.setApartamento(apartamentoSelecionado);
		usuario.setAdm(false);
		try {
			usuario = usuarioService.salvar(usuario);
			tokenService.invalidar(token);
		} catch (Exception e) {
			e.printStackTrace();
			try{
				emailSuporte.enviarEmail("Ocorreu um erro inesperado confirmar o usuário.", e.getMessage(), null);
			}catch(Exception e1){
			}
		}

		cadastrouApto = true;
	}

	public Boolean condominioPossuiAgrupamento(){
		if(condominioPossuiAgrupamento == null){
			condominioPossuiAgrupamento = condominioService.condominioPossuiAgrupamento(usuario.getCondominio().getId());
		}
		return condominioPossuiAgrupamento;
	}


	public void recuperarBlocosPorAgrupamento() {
		if(agrupamentoSelecionado != null){
			blocos = condominioService.recuperarTodosBlocosPorAgrupamento(agrupamentoSelecionado.getId());
			if(blocos != null && blocos.size() == 1){
				blocoSelecionado = blocos.get(0);
			}
		}
	}


	/* GETTERS e SETTERS*/

	public boolean isUsuarioConfirmado() {
		return usuarioConfirmado;
	}

	public void setUsuarioConfirmado(boolean usuarioConfirmado) {
		this.usuarioConfirmado = usuarioConfirmado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public AgrupamentoUnidades getAgrupamentoSelecionado() {
		return agrupamentoSelecionado;
	}

	public void setAgrupamentoSelecionado(AgrupamentoUnidades agrupamentoSelecionado) {
		this.agrupamentoSelecionado = agrupamentoSelecionado;
	}

	public Bloco getBlocoSelecionado() {
		return blocoSelecionado;
	}

	public void setBlocoSelecionado(Bloco blocoSelecionado) {
		this.blocoSelecionado = blocoSelecionado;
	}

	public Apartamento getApartamentoSelecionado() {
		return apartamentoSelecionado;
	}

	public void setApartamentoSelecionado(Apartamento apartamentoSelecionado) {
		this.apartamentoSelecionado = apartamentoSelecionado;
	}

	public List<AgrupamentoUnidades> getAgrupamentos() {
		return agrupamentos;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public boolean isCadastrouApto() {
		return cadastrouApto;
	}


}

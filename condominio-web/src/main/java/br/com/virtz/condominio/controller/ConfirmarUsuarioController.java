package br.com.virtz.condominio.controller;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfirmarUsuarioController {
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private NavigationPage navegacao;
	

	private boolean usuarioConfirmado;
	private boolean condominioExistente = Boolean.FALSE;
	private boolean sucesso = Boolean.FALSE;
	
	private List<Condominio> condominios = null;
	private List<Bloco> blocos = null;
	private Condominio condominioSelecionado;
	private Bloco blocoSelecionado;
	private Apartamento apartamentoSelecionado;
	
	private List<Cidade> cidades;
	private Cidade cidadeSelecionada;
	
	private Usuario usuario = null;
	
	
	@PostConstruct
	public void init(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
 
		String token = request.getParameter("token");
		
		// verifica se o token é valido
		if(tokenService.tokenEhValido(token)){
			Token tokenEntidade = tokenService.recuperarToken(token);
			
			// recuperar o id do usuario para busca-lo no banco
			String chave = tokenEntidade.getChaveEntidade();
			if(StringUtils.isNotBlank(chave)){
				usuario = usuarioService.recuperarUsuarioCompleto(Long.valueOf(chave));
				
				// se o usuário for o primeiro usuário do condominio
				if(usuario.getCondominio() != null){
					condominioExistente = Boolean.TRUE;
				}
				usuario.setCadastroConfirmado(Boolean.TRUE);
				try {
					usuario = usuarioService.salvar(usuario);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			tokenService.invalidar(token);
			cidades = condominioService.cidadesQuePossuemCondominioCadastrado();
			Collections.sort(cidades);
			cidadeSelecionada = null;
			condominioSelecionado = null;
			blocoSelecionado = null;
			apartamentoSelecionado = null;
			usuarioConfirmado = Boolean.TRUE;
		} else {
			usuarioConfirmado = Boolean.FALSE;
		}
				
	}
	
	
	public void recuperarCondominios(){
		if(this.cidadeSelecionada != null){
			condominios = condominioService.recuperarPorCidade(this.cidadeSelecionada.getId());
		}
	}
	
	
	public void recuperarBlocos(){
		if(this.condominioSelecionado != null){
			blocos = condominioService.recuperarTodosBlocosComApartamentos(this.condominioSelecionado.getId());
		}
	}
	
	
	public void cadastrarCondominio(){
		navegacao.redirectToPage("/condominio/cadastrarCondominio.faces");
	}
	
	
	public void login(){
		navegacao.redirectToPage("/login.faces");
	}
	
	
	public void selecionarCondominio() throws CondominioException{
		if(this.condominioSelecionado != null){
			try {
				usuario.setCondominio(condominioSelecionado);
				usuarioService.salvar(usuario);
				sucesso = Boolean.TRUE;
			} catch (Exception e) {
				throw new CondominioException("Ocorreu um erro ao selecionar o condomínio. Favor tentar novamente.");
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

	public boolean isCondominioExistente() {
		return condominioExistente;
	}

	public void setCondominioExistente(boolean condominioExistente) {
		this.condominioExistente = condominioExistente;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public Cidade getCidadeSelecionada() {
		return cidadeSelecionada;
	}

	public void setCidadeSelecionada(Cidade cidadeSelecionada) {
		this.cidadeSelecionada = cidadeSelecionada;
	}

	public List<Condominio> getCondominios() {
		return condominios;
	}

	public void setCondominios(List<Condominio> condominios) {
		this.condominios = condominios;
	}

	public Condominio getCondominioSelecionado() {
		return condominioSelecionado;
	}

	public void setCondominioSelecionado(Condominio condominioSelecionado) {
		this.condominioSelecionado = condominioSelecionado;
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
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
	
	
}

package br.com.virtz.condominio.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class SelecionarCondominioUsuarioController {
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
	

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
		Object usuarioEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idUsuario");
		usuario = usuarioService.recuperarUsuarioCompleto(Long.parseLong(usuarioEditar.toString()));
				
		// se o usuário for o primeiro usuário do condominio
		if(usuario.getCondominio() != null){
			condominioExistente = Boolean.TRUE;
		}
		
		cidades = condominioService.cidadesQuePossuemCondominioCadastrado();
		Collections.sort(cidades);
		cidadeSelecionada = null;
		condominioSelecionado = null;
		blocoSelecionado = null;
		apartamentoSelecionado = null;
				
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
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idUsuario", usuario.getId());
		navegacao.redirectToPage("/condominio/cadastrarCondominio.faces");
	}
	
	
	public void login(){
		navegacao.redirectToPage("/login.faces");
	}
	
	
	public void selecionarCondominio() throws CondominioException{
		if(this.condominioSelecionado != null){
			try {
				usuario.setCondominio(condominioSelecionado);
				usuario.setApartamento(getApartamentoSelecionado());
				usuarioService.salvar(usuario);
				sucesso = Boolean.TRUE;
				try{
					enviarEmailNovoMoradorParaSindico(usuario, condominioSelecionado);
				}catch(Exception e ){
					e.printStackTrace();
					// nada
				}
			} catch (Exception e) {
				throw new CondominioException("Ocorreu um erro ao selecionar o condomínio. Favor tentar novamente.");
			}
		}
	}

	
	private void enviarEmailNovoMoradorParaSindico(Usuario usuario, Condominio condominio) {
		
		// recuperar sindico
		List<Usuario> sindicos = usuarioService.recuperarSindicos(condominio.getId());
		if(sindicos == null){
			return;
		}
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();

		for(Usuario sindico : sindicos){
			Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
			mapParametrosEmail.put("nomeUsuario", sindico.getNome());
			mapParametrosEmail.put("nomeCondominio", condominio.getNome());
			mapParametrosEmail.put("nomeNovoUsuario", usuario.getNome());
			mapParametrosEmail.put("numeroApto", (usuario.getApartamento() != null) ? usuario.getApartamento().getNomeExibicao() : "-");
			mapParametrosEmail.put("numeroBloco", (usuario.getApartamento() != null && usuario.getApartamento().getBloco() != null) ? usuario.getApartamento().getBloco().getNome() : "-");
			
			String msg = leitor.processarTemplate(caminho, EnumTemplates.NOVO_MORADOR.getNomeArquivo(), mapParametrosEmail);
			
			Email email = new Email(EnumTemplates.NOVO_MORADOR.getDe(), sindico.getEmail(), EnumTemplates.NOVO_MORADOR.getAssunto(), msg);
			enviarEmail.enviar(email);
		}
	}
	
	
	/* GETTERS e SETTERS*/
	
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

package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Estado;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ILocalidadeService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarCondominioController {
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ILocalidadeService localidadeService;
	
	@Inject
	private MessageHelper mensagem;
	
	@Inject
	private NavigationPage navegacao;
	
	
	private Condominio condominio;
	private Usuario usuario;
	private List<Bloco> blocos;
	private Integer quantidadeBlocos;
	
	private List<Estado> estados;
	private List<Cidade> cidades;
	private Estado estadoSelecionado;
	
	
	@PostConstruct
	public void init(){
		//String idUsuario = "29";
		Object idUsuario = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idUsuario");
		
		usuario = usuarioService.recuperarUsuarioCompleto(Long.parseLong(idUsuario.toString()));
		
		condominio = new Condominio();
		blocos = null;
		quantidadeBlocos = 1;
		
		estados = localidadeService.recuperarTodosEstados();
		cidades = null;
	}
	
	
	public boolean mostrarCadastrosBlocos(){
		if(blocos == null || blocos.isEmpty()){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public void recuperarCidades(){
		if(estadoSelecionado != null){
			cidades = localidadeService.recuperarPorEstado(estadoSelecionado.getId());
		}
	}
	
	public void sugerirBlocos(){
		blocos = condominioService.sugerirBlocos(quantidadeBlocos, condominio);		
	}
	
	
	public void removerBloco(Bloco bloco){
		if(blocos != null && !blocos.isEmpty()){
			blocos.remove(bloco);
		}
	}
	
	public void novoBloco(){
		if(blocos != null ){
			Bloco b = new Bloco();
			b.setCondominio(condominio);
			b.setNome("Bloco "+(blocos.size()+1));
			if(!blocos.isEmpty()){
				b.setQuantidadeAndares(blocos.get(0).getQuantidadeAndares());
			} else {
				b.setQuantidadeAndares(4);
			}
			blocos.add(b);
		}
	}
	
	public void salvar() throws AppException{
		
		try {
			// salva condominio
			condominio = condominioService.salvar(condominio);
			
			// atualiza usuario 
			usuario.setCondominio(condominio);
			usuarioService.salvar(usuario);
			
			// salva os blocos
			for(Bloco bloco : blocos){
				bloco.setCondominio(condominio);
				condominioService.salvarBloco(bloco);
			}
			
			// redireciona para cadastro de apartamentos
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idUsuario", usuario.getId());
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idCondominio", condominio.getId());
			navegacao.redirectToPage("/condominio/cadastrarApartamentos.faces");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
		}

	}
	
	
	public void onRowEdit(RowEditEvent event) {
        mensagem.addInfo("Bloco editado "+ ((Bloco) event.getObject()).getId());
    }
     
    public void onRowCancel(RowEditEvent event) {
    	mensagem.addInfo("Edição cancelada!");
    }


    
    
    // GETTERS E SETTERS
	public Condominio getCondominio() {
		return condominio;
	}

	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}

	public Integer getQuantidadeBlocos() {
		return quantidadeBlocos;
	}

	public void setQuantidadeBlocos(Integer quantidadeBlocos) {
		this.quantidadeBlocos = quantidadeBlocos;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public Estado getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(Estado estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}
		    
}

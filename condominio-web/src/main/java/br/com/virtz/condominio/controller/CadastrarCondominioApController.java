package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarCondominioApController {
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private IUsuarioService usuarioService;
	
	@Inject
	private MessageHelper mensagem;
	
	@Inject
	private NavigationPage navegacao;
	
	
	//private Condominio condominio;
	private Usuario usuario;
	private List<Bloco> blocos;
	

	
	@PostConstruct
	public void init(){
		//Object idUsuario = "29";
		Object idUsuario = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idUsuario");
				
		// Não preciso do id do condominio. Como o usuário já foi atualizado ele já tem vinculo com o condominio.
		//Object idCondominio = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idCondominio");
		
		usuario = usuarioService.recuperarUsuarioCompleto(Long.parseLong(idUsuario.toString()));
		//condominio = condominioService.recuperarCondominioCompleto(usuario);
		blocos = condominioService.recuperarTodosBlocosComApartamentos(usuario.getCondominio().getId());
		
		
		Integer qtdBlocos = blocos.size();
		for(Bloco bloco : blocos){

			// montar uma sugestão de apartamentos
			if(bloco.getApartamentos() == null || bloco.getApartamentos().isEmpty()){
				List<Apartamento> apartamentosBloco = new ArrayList<Apartamento>();
				
				Integer qtdAptosPorAndar = 4; // valor default
				if(usuario.getCondominio().getQuantidadeApartamentos() != null){
					qtdAptosPorAndar = (usuario.getCondominio().getQuantidadeApartamentos() / qtdBlocos) /  bloco.getQuantidadeAndares();
					if(qtdAptosPorAndar < 1){
						qtdAptosPorAndar = 1;
					}
				}
				
				// cria os apartamentos por andar
				for(int andar = 1; andar <= bloco.getQuantidadeAndares(); andar++){
					for(int apto = 1; apto <= qtdAptosPorAndar; apto++){
						Apartamento ap = new Apartamento();
						ap.setAndar(andar);
						ap.setNumero(andar+"0"+apto);
						apartamentosBloco.add(ap);
					}
				}
				
				bloco.setApartamentos(apartamentosBloco);
			}
		}
		
	}
	
	
	public void novoApartamento(Bloco bloco){
		if(bloco.getApartamentos() == null){
			bloco.setApartamentos(new ArrayList<Apartamento>());
		}
		
		bloco.getApartamentos().add(new Apartamento());
	}
	
	
	public void removerApartamento(Bloco bloco, Apartamento ap){
		if(bloco.getApartamentos() != null){
			bloco.getApartamentos().remove(ap);
		}
	}
	
	
	public void salvar() throws AppException{
		
		try {
			
			//TODO : remover todos os blocos
			
			// adicionar os blocos
			for(Bloco bloco : blocos){
				condominioService.salvarBloco(bloco);
			}
			
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
		}

	}
	
	
	public void login(){
		navegacao.redirectToPage("/login.faces");
	}
    
    
    // GETTERS E SETTERS
	
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

		    
}

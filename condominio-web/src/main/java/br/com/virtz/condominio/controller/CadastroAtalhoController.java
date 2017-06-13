package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumFuncionalidadeAtalho;
import br.com.virtz.condominio.entidades.Atalho;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IAtalhoService;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroAtalhoController {

	@EJB
	private ICondominioService condominioService;

	@EJB
	private IAtalhoService atalhoService;

	@EJB
	private IUsuarioService usuarioService;

	@Inject
	private PrincipalController principalController;

	@Inject
	private NavigationPage navegacao;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper message;


	private List<Atalho> atalhos = null;
	private Atalho atalho = null;

	private Usuario usuario = null;

	private String tipo;
	private Map<String,String> tipos = new HashMap<String, String>();




	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		
		iniciarAtalho();
		atalhos = atalhoService.recuperarTodos(usuario.getCondominio());

		tipos.put(EnumFuncionalidadeAtalho.NOTIFICAR_PORTARIA.getDescricao(), EnumFuncionalidadeAtalho.NOTIFICAR_PORTARIA.toString());
	}



	protected void iniciarAtalho() {
		atalho = new Atalho();
		atalho.setCondominio(sessao.getUsuarioLogado().getCondominio());
	}

	

	public void salvar(ActionEvent event) throws CondominioException {
		if(atalho == null){
			return;
		}

		try{

			
			EnumFuncionalidadeAtalho funcionalidade = EnumFuncionalidadeAtalho.recuperarPorDescricao(tipo);
			if(funcionalidade == null){
				funcionalidade = EnumFuncionalidadeAtalho.valueOf(tipo);
			}
			
			atalho.setFuncionalidade(funcionalidade);

			if(atalho.getId() == null){
				Atalho a = atalhoService.recuperarTodos(atalho.getCondominio(), atalho.getFuncionalidade());
				if(a != null){
					this.message.addError("Já existe um atalho para essa funcionalidade.");
					return;
				}
			}
						
			atalho = atalhoService.salvar(atalho);
			
			message.addInfo("Atalho salvo com sucesso");
			
			atalhos = atalhoService.recuperarTodos(atalho.getCondominio());
			iniciarAtalho();
		}catch(Exception e){
			e.printStackTrace();
			this.message.addError("Ocorreu um erro ao salvar atalho.");
		}
	}

	
	public void remover(Atalho atalho) throws CondominioException {
		if(atalho == null){
			return;
		}

		atalhoService.remover(atalho.getId());
		message.addInfo("Atalho excluído com sucesso");
		
		atalhos = atalhoService.recuperarTodos(sessao.getUsuarioLogado().getCondominio());
		iniciarAtalho();
		
	}
	



	/* GETTERS e SETTERS*/

	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Map<String, String> getTipos() {
		return tipos;
	}

	public void setTipos(Map<String, String> tipos) {
		this.tipos = tipos;
	}

	public Condominio getCondominio(){
		return usuario.getCondominio();
	}

	public List<Atalho> getAtalhos() {
		return atalhos;
	}

	public void setAtalhos(List<Atalho> atalhos) {
		this.atalhos = atalhos;
	}

	public Atalho getAtalho() {
		return atalho;
	}

	public void setAtalho(Atalho atalho) {
		this.atalho = atalho;
	}
	
	

}

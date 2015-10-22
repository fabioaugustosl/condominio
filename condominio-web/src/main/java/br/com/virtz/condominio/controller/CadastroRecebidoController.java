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

import br.com.virtz.condominio.constantes.EnumTipoNotificacao;
import br.com.virtz.condominio.constantes.EnumTipoRecebido;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.service.IRecebidoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroRecebidoController {
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private IRecebidoService recebidoService;
	
	@EJB
	private INotificacaoService notificacaoService;
	
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	

	private List<Bloco> blocos = null;
	private Bloco blocoSelecionado;
	private Apartamento apartamentoSelecionado;
	
	private Usuario usuario = null;
	
	private String tipo;  
	private Map<String,String> tipos = new HashMap<String, String>();
	
	private String descricao = null;  
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
				
		blocoSelecionado = null;
		blocos = condominioService.recuperarTodosBlocosComApartamentos(usuario.getCondominio().getId());
		if(blocos != null &&
				blocos.size() == 1){
			blocoSelecionado = blocos.get(0);
		}
		apartamentoSelecionado = null;
		
		tipos.put(EnumTipoRecebido.CORRESPONDENCIA.toString(), EnumTipoRecebido.CORRESPONDENCIA.getDescricao());
		tipos.put(EnumTipoRecebido.ENCOMENDA.toString(), EnumTipoRecebido.ENCOMENDA.getDescricao());
		 
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		try{
			EnumTipoRecebido tipoRecebido = EnumTipoRecebido.recuperarPorDescricao(tipo);
			if(tipoRecebido.equals(EnumTipoRecebido.CORRESPONDENCIA)){
				recebidoService.salvarNovaCorrespondencia(apartamentoSelecionado.getId(), descricao);
				notificacaoService.salvarNovaNotificacao(usuario.getCondominio(), usuario, EnumTipoNotificacao.CORRESPONDENCIA, null);
			} else {
				recebidoService.salvarNovaEncomenda(apartamentoSelecionado.getId(), descricao);
				notificacaoService.salvarNovaNotificacao(usuario.getCondominio(), usuario, EnumTipoNotificacao.ENCOMENDA, null);
			}
			
			message.addInfo("A correspondência/encomenda foi salva com sucesso.");
			descricao = null;
			apartamentoSelecionado = null;
			tipo = null;
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a correspondência/encomenda.");
		}
	}
	
	public void voltar(){
		navegacao.redirectToPage("/portaria/gerenciarPortaria.faces");
	}
	
	
	
	/* GETTERS e SETTERS*/

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}

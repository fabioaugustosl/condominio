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
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.service.IRecebidoService;
import br.com.virtz.condominio.service.IUsuarioService;
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
	private IUsuarioService usuarioService;
	
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
	private Apartamento apartamento;
	
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
		apartamento = null;
		
		tipos.put(EnumTipoRecebido.CORRESPONDENCIA.toString(), EnumTipoRecebido.CORRESPONDENCIA.getDescricao());
		tipos.put(EnumTipoRecebido.ENCOMENDA.toString(), EnumTipoRecebido.ENCOMENDA.getDescricao());
		 
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		if(apartamento == null){
			return;
		}
		
		try{
			EnumTipoRecebido tipoRecebido = EnumTipoRecebido.recuperarPorDescricao(tipo);
			if(tipoRecebido.equals(EnumTipoRecebido.CORRESPONDENCIA)){
				recebidoService.salvarNovaCorrespondencia(usuario.getCondominio().getId(), apartamento.getId(), descricao);
			} else {
				recebidoService.salvarNovaEncomenda(usuario.getCondominio().getId(), apartamento.getId(), descricao);
			}
			
			enviarNotificacaoParaMoradores(tipoRecebido, apartamento);
			
			message.addInfo("A correspondência/encomenda foi salva com sucesso.");
			descricao = null;
			apartamento = null;
			tipo = null;
			blocoSelecionado = null;
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a correspondência/encomenda.");
		}
	}

	/**
	 * Enviar notificação e email para os moradores avisando que chegou a correspondencia.
	 * 
	 * @param tipoRecebido
	 * @throws AppException
	 */
	private void enviarNotificacaoParaMoradores(EnumTipoRecebido tipoRecebido, Apartamento apartamento) throws AppException {
		if(apartamento == null){
			return;
		}
		
		List<Usuario> usuariosRecebimento = usuarioService.recuperarUsuariosPorApartamento(apartamento.getId());
		
		for(Usuario u : usuariosRecebimento){
			notificacaoService.salvarNovaNotificacao(usuario.getCondominio(), u, (tipoRecebido.equals(EnumTipoRecebido.CORRESPONDENCIA) ? EnumTipoNotificacao.CORRESPONDENCIA : EnumTipoNotificacao.ENCOMENDA), null);
			// TODO : enviar email avisando da correspondencia também!!!
		}
	}
	
	
	public void voltar(){
		navegacao.redirectToPage("/portaria/gerenciarPortaria.faces");
	}
	
	
	public void irParaListagem(){
		navegacao.redirectToPage("/portaria/listagemCorrespondencias.faces");
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
		return apartamento;
	}

	public void setApartamentoSelecionado(Apartamento apartamentoSelecionado) {
		this.apartamento = apartamentoSelecionado;
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

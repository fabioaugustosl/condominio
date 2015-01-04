package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.controller.util.UtilTipoVotacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IVotacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class VotacaoController {
	
	@EJB
	private IVotacaoService votacaoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	private Votacao votacao;
	private String tipoVotacaoSelecionado;
	private UtilTipoVotacao utilTipoVotacao;
		
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		utilTipoVotacao = new UtilTipoVotacao();
	}

	
	/* GETTERS E SETTERS*/
		
	public Votacao getVotacao() {
		return votacao;
	}
	
	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}

	public List<String> getTiposVotacao() {
		return null;
	}

	public String getTipoVotacaoSelecionado() {
		return tipoVotacaoSelecionado;
	}

	public void setTipoVotacaoSelecionado(String tipoVotacaoSelecionado) {
		this.tipoVotacaoSelecionado = tipoVotacaoSelecionado;
	}

	public boolean isSimNao() {
		return utilTipoVotacao.isSimNao();
	}

	public boolean isData() {
		return utilTipoVotacao.isData();
	}

	public boolean isMoeda() {
		return utilTipoVotacao.isMoeda();
	}

	public boolean isNumerico() {
		return utilTipoVotacao.isNumerico();
	}

	public boolean isOpcoes() {
		return utilTipoVotacao.isOpcoes();
	}

	public UtilTipoVotacao getUtilTipoVotacao() {
		return utilTipoVotacao;
	}
	
}


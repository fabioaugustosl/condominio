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
public class CadastroVotacaoController {
	
	@EJB
	private IVotacaoService votacaoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	private Votacao votacao;
	private List<String> tiposVotacao;
	private String tipoVotacaoSelecionado;
	private UtilTipoVotacao utilTipoVotacao;
		
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		montarComboTipoVotacao();
		utilTipoVotacao = new UtilTipoVotacao();
		votacao = votacaoService.criarNovaVotacao(usuario, usuario.getCondominio(), null, null);
	}

	private void montarComboTipoVotacao(){
		tiposVotacao = new ArrayList<String>();
		for(EnumTipoVotacao tipoVotacao : EnumTipoVotacao.values()){
			tiposVotacao.add(tipoVotacao.getDescricao());
		}
	}

	public void montarOpcaoTipoVotacao(){
		if(StringUtils.isNotBlank(tipoVotacaoSelecionado)){
			EnumTipoVotacao tipoVotacao = EnumTipoVotacao.recuperarPorDescricao(tipoVotacaoSelecionado);
			utilTipoVotacao.tratarTipoVotacaoExibicao(tipoVotacao);
		}
	}
	
	
	/* GETTERS E SETTERS*/
		
	public Votacao getVotacao() {
		return votacao;
	}
	
	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}

	public List<String> getTiposVotacao() {
		return tiposVotacao;
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
}


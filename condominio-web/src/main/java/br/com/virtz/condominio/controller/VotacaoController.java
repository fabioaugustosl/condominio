package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacaoComImagem;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ParametroObrigatorioNuloException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.geral.UtilTipoVotacao;
import br.com.virtz.condominio.geral.VotacaoView;
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
	
	@Inject
	private EnviarEmailSuporteController emailSuporte;
	
	
	private Votacao votacao;
	private List<Votacao> votacoes;
	private List<VotacaoView> votacoesView;
	private List<VotacaoView> votacoesViewImagem;
	private String tipoVotacaoSelecionado;
	private VotacaoView votacaoViewResultado = null;
	private VotacaoView votacaoViewImagemResultado = null;
	
	private Usuario usuario = null;
			
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		montarVotacoesView(usuario);
	}



	private void montarVotacoesView(Usuario usuario) {
		votacoesView = new ArrayList<VotacaoView>();
		votacoesViewImagem = new ArrayList<VotacaoView>();
		
		votacoes = votacaoService.recuperarTodasAtivas(usuario.getCondominio());
		if(votacoes != null && !votacoes.isEmpty()){
			for(Votacao v : votacoes){
				VotacaoView vv = new VotacaoView(v);
				vv.getUtil().tratarTipoVotacaoExibicao(v.getTipoVotacao());
				
				try {
					if(v.isResultadoParcial()){
						vv.setResultadoVotacaoSelecionada(votacaoService.recuperarResultado(v.getId()));
					}
				} catch (AppException e1) {
					// ocorreu um erro ao recuperar o resultado parcial
					try{
						emailSuporte.enviarEmail("ocorreu um erro ao recuperar o resultado parcial.", e1.getMessage(), usuario.getCondominio().getId());
					}catch(Exception e2){
					}
				}
				
				Voto votoUsuario = null;
				try {
					votoUsuario = votacaoService.recuperarVotoPorApto(v, usuario);
					if(votoUsuario != null){
						vv.setVotou(Boolean.TRUE);
					}
				} catch (ParametroObrigatorioNuloException e) {
					try{
						emailSuporte.enviarEmail("ocorreu um erro ao recuperar voto por apto.", e.getMessage(), usuario.getCondominio().getId());
					}catch(Exception e2){
					}
				}
				
				if(v.isVotacaoPorOpcaoImagem()){
					votacoesViewImagem.add(vv);
				} else {
					votacoesView.add(vv);
				}
			}
		}
	
	}
	
	
	
	public void votar(VotacaoView votacaoView) {
		Usuario usuario =  sessao.getUsuarioLogado();
		UtilTipoVotacao util = votacaoView.getUtil();
		
		Voto voto = new Voto();
		voto.setVotacao(votacaoView.getVotacao());
		voto.setDataVotacao(new Date());
		voto.setUsuario(usuario);
		
		if(util.isData()){
			voto.setData(util.getValorData());
		} else if(util.isMoeda()){
			voto.setMoeda(util.getValorMoeda());
		} else if(util.isNumerico()){
			
			if(util.getValorNumerico().isNaN()){
				message.addError("O valor digitado não é um número.");
				return;
			} else {
				voto.setNumero(util.getValorNumerico());
			}
		} else if(util.isSimNao()){
			voto.setSim(util.isValorSimNao());
		} else if(util.isOpcoes()){
			OpcaoVotacao opcao = votacaoService.recuperarOpcao(util.getIdValorOpcao());
			voto.setOpcao(opcao);
		}  
		
		try {
			
			// valida se já foi computado voto para o apto. Apenas 1 voto por apto é aceito.
			Voto votoComputadoApto = votacaoService.recuperarVotoPorApto(votacaoView.getVotacao(), usuario);
			
			if(votoComputadoApto == null){
				votacaoService.votar(voto);
				montarVotacoesView(usuario);
				message.addInfo("Seu voto foi computado!");
			} else {
				message.addInfo("Voto inválido. Um voto para seu apto já foi computado.");
			}
			
		} catch (AppException e) {
			try{
				emailSuporte.enviarEmail("Ocorreu um erro ao salvar a votação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e2){
			}
			message.addError("Ocorreu um erro ao salvar a votação.");
		}
	}
	
	public void votar(VotacaoView votacaoView, OpcaoVotacaoComImagem opcao) {
		Usuario usuario =  sessao.getUsuarioLogado();
		UtilTipoVotacao util = votacaoView.getUtil();
		
		Voto voto = new Voto();
		voto.setVotacao(votacaoView.getVotacao());
		voto.setDataVotacao(new Date());
		voto.setUsuario(usuario);
		
		//OpcaoVotacaoComImagem opcao = votacaoService.recuperarOpcao(util.getIdValorOpcao());
		voto.setOpcaoComImagem(opcao);
		
		try {
			
			// valida se já foi computado voto para o apto. Apenas 1 voto por apto é aceito.
			Voto votoComputadoApto = votacaoService.recuperarVotoPorApto(votacaoView.getVotacao(), usuario);
			
			if(votoComputadoApto == null){
				votacaoService.votar(voto);
				montarVotacoesView(usuario);
				message.addInfo("Seu voto foi computado!");
			} else {
				message.addInfo("Voto inválido. Um voto para seu apto já foi computado.");
			}
			
		} catch (AppException e) {
			try{
				emailSuporte.enviarEmail("Ocorreu um erro ao salvar a votação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e2){
			}
			message.addError("Ocorreu um erro ao salvar a votação.");
		}
	}
	
	
	public boolean possuiVotacaoEmAndamento(){
		if(votacoesView == null || votacoesView.isEmpty()){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public boolean possuiVotacaoPorImagemEmAndamento(){
		if(votacoesViewImagem == null || votacoesViewImagem.isEmpty()){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
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

	public boolean isSimNao(UtilTipoVotacao util) {
		return util.isSimNao();
	}
	
	public boolean simNao(UtilTipoVotacao util) {
		return util.isSimNao();
	}

	public boolean isData(UtilTipoVotacao util) {
		return util.isData();
	}

	public boolean isMoeda(UtilTipoVotacao util) {
		return util.isMoeda();
	}

	public boolean isNumerico(UtilTipoVotacao util) {
		return util.isNumerico();
	}

	public boolean isOpcoes(UtilTipoVotacao util) {
		return util.isOpcoes();
	}

	public List<Votacao> getVotacoes() {
		return votacoes;
	}

	public void setVotacoes(List<Votacao> votacoes) {
		this.votacoes = votacoes;
	}

	public List<VotacaoView> getVotacoesView() {
		return votacoesView;
	}

	public void setVotacoesView(List<VotacaoView> votacoesView) {
		this.votacoesView = votacoesView;
	}

	public VotacaoView getVotacaoViewResultado() {
		return votacaoViewResultado;
	}

	public void setVotacaoViewResultado(VotacaoView votacaoViewResultado) {
		this.votacaoViewResultado = votacaoViewResultado;
	}

	public List<VotacaoView> getVotacoesViewImagem() {
		return votacoesViewImagem;
	}

	public void setVotacoesViewImagem(List<VotacaoView> votacoesViewImagem) {
		this.votacoesViewImagem = votacoesViewImagem;
	}

	public VotacaoView getVotacaoViewImagemResultado() {
		return votacaoViewImagemResultado;
	}

	public void setVotacaoViewImagemResultado(VotacaoView votacaoViewImagemResultado) {
		this.votacaoViewImagemResultado = votacaoViewImagemResultado;
	}
	
	
}


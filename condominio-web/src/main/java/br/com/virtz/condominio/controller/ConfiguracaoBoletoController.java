package br.com.virtz.condominio.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.virtz.condominio.entidades.ConfiguracaoBoleto;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.DataUtil;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ConfiguracaoBoletoController {

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private IFinanceiroService financeiroService;
	
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Usuario usuario = null;
	private List<Usuario> usuarios = null;
	
	private ConfiguracaoBoleto configuracao = null;
	private Double valorBase = null;
	
	private Integer ano = null;
	private Integer mes = null;
	private List<Integer> anos = null;
	private Map<Integer, String> meses = null;
	
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		configuracao = new ConfiguracaoBoleto();
		
		valorBase = usuario.getCondominio().getValorCondominioMes();
		if(valorBase == null){
			valorBase = 0d;
		}
		DataUtil dataUtil = new DataUtil();
		meses = dataUtil.listarMesesSelecao();
		anos = dataUtil.listarAnosSelecao(null);
		this.ano = dataUtil.agora().get(Calendar.YEAR);
		this.mes = dataUtil.agora().get(Calendar.MONTH+1);
	}
	
	public void montarCobranca(){
		
		if(this.mes == null){
			message.addError("Favor selecionar o MÊS de referência.");
			return;
		}
		if(this.ano == null){
			message.addError("Favor selecionar o ANO de referência.");
			return;
		}
		
		configuracao = financeiroService.recuperarConfiguracaoPorCondominio(usuario.getCondominio().getId(), this.ano, this.mes);
		
		if(configuracao == null){
			configuracao = new ConfiguracaoBoleto();
			configuracao.setValorBase(valorBase);
			configuracao.setCondominio(usuario.getCondominio());
			configuracao.setMes(mes);
			configuracao.setAno(ano);
		}
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		try{
			
			financeiroService.salvar(configuracao);
			message.addInfo("A configuração foi salva com sucesso.");

		}catch(AppException appE){
			throw new CondominioException(appE.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a configuração do boleto.");
		}
	}
	
	
	public void gerarCobranca(ActionEvent event) throws CondominioException {
		try{
			
			// TODO : Pra cadas usuario do condominio gerar uma cobrança
			
			message.addInfo("A cobrança foi gerada com sucesso.");
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a configuração do boleto.");
		}
	}

	
	
	/* GETTER E SETTERS*/
	
	public ConfiguracaoBoleto getConfiguracao() {
		return configuracao;
	}

	public Double getValorBase() {
		return valorBase;
	}

	public void setValorBase(Double valorBase) {
		this.valorBase = valorBase;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public List<Integer> getAnos() {
		return anos;
	}

	public Map<Integer, String> getMeses() {
		return meses;
	}
	
		
}

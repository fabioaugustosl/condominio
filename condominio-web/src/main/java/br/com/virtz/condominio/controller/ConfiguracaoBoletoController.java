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

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.entidades.ConfiguracaoBoleto;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ConfiguracaoBoletoController {

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private IFinanceiroService financeiroService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	
	private Usuario usuario = null;
	
	private ConfiguracaoBoleto configuracao = null;
	private Double valorBase = null;
	
	private Integer ano = null;
	private Integer mes = null;
	private List<Integer> anos = null;
	private Map<Integer, String> meses = null;
	
	private ContaBancariaCondominio conta = null;
	
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		
		valorBase = usuario.getCondominio().getValorCondominioMes();
		if(valorBase == null){
			valorBase = 0d;
			message.addWarn("O valor do condomínio está zerado! Para configurar o valor base do condomínio acesse o menu Configurações > Meu Condominio ");
		}
		
		conta = condominioService.recuperarContaBancariaCondominioPrincipal(usuario.getCondominio().getId());
		if(conta == null){
			message.addWarn("Para realizar um lançamento de um título é necessário configurar a conta bancária do condomínio.");
		} else {
			carregarInformacoesPeriodoPadroes();
			montarConfiguracao();
		}
	}

	private void carregarInformacoesPeriodoPadroes() {
		DataUtil dataUtil = new DataUtil();
		meses = dataUtil.listarMesesSelecao();
		anos = dataUtil.listarAnosSelecao(null);
		this.ano = dataUtil.agora().get(Calendar.YEAR);
		this.mes = dataUtil.agora().get(Calendar.MONTH)+1;
	}

	private void criarNovaConfiguracao() {
		
		configuracao = new ConfiguracaoBoleto();
		configuracao.setCondominio(usuario.getCondominio());
		configuracao.setValorBase(valorBase);
		configuracao.setMes(mes);
		configuracao.setAno(ano);
		
		String nomeMes = meses.get(mes);
		
		message.addWarn("O lançamento do título para "+nomeMes+"/"+ano+" ainda não foi SALVO. Basta preencher os dados e clicar no botão salvar.");
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
		
		montarConfiguracao();
	}

	private void montarConfiguracao() {
		configuracao = financeiroService.recuperarConfiguracaoPorCondominio(usuario.getCondominio().getId(), this.ano, this.mes);
		
		if(configuracao == null){
			criarNovaConfiguracao();
		} 
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		try{
			configuracao.setAno(this.ano);
			configuracao.setMes(this.mes);
			financeiroService.salvar(configuracao);
			
			message.addWarn("Para que os moradores consigam emitir o boleto é necessário clicar em GERAR COBRANÇA. Antes, confira todos os dados do título.");
			message.addInfo("O lançamento do título foi salvo com sucesso.");
		}catch(AppException appE){
			message.addError(appE.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar o lançamento do título.");
		}
	}
	
	
	public void gerarCobranca(ActionEvent event) throws CondominioException {
		salvar(event);
		
		try{
			
			if(configuracao == null || configuracao.getId() == null){
				message.addError("Você deve salvar a configuração do título primeiro.");
				return;
			}
			financeiroService.gerarCobrancas(configuracao);
			
			message.addInfo("A cobrança(s) foi(ram) gerada(s) com sucesso.");
		}catch(AppException appE){
			message.addError(appE.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao gerar as cobranças para os títulos.");
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
	
	public boolean exibirJuros(){
		return usuario.getCondominio().isJurosAposVencimento();
	}
	
	public boolean exibirMulta(){
		return usuario.getCondominio().isMultaAposVencimento();
	}

	public ContaBancariaCondominio getConta() {
		return conta;
	}
			
}

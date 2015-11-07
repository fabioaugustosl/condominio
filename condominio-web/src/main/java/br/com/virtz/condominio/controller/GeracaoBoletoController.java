package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.boleto.GeradorNossoNumero;
import br.com.virtz.boleto.IFabricaBoletos;
import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoEndereco;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;
import br.com.virtz.boleto.bean.NossoNumero;
import br.com.virtz.condominio.boleto.conversor.ConversorDadosBoleto;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.ErroConversaoException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class GeracaoBoletoController {

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
	
	@Inject
	private ConversorDadosBoleto conversorBoleto;
	
	@Inject
	private GeradorNossoNumero geradorNN;
	
	
	@Inject
	private IFabricaBoletos fabrica;
	
	private List<CobrancaUsuario> cobrancas = null;
	private Usuario usuario = null;
	
	private List<String> anosMeses = null;
	private String anoMes= null;

	private InfoTitulo infoTitulo;
	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		anosMeses  = financeiroService.recuperarAnosMesesDispiniveis(usuario.getCondominio().getId());
	}
	

	public void listarUsuarios(){
		cobrancas = new ArrayList<CobrancaUsuario>();
		if(StringUtils.isNotBlank(anoMes)){
			String[] anoMesSelecionado = anoMes.split("/");
			cobrancas =  financeiroService.recuperarCobrancas(usuario.getCondominio().getId(), Integer.parseInt(anoMesSelecionado[0]), Integer.parseInt(anoMesSelecionado[1]));
		}
		
	}
	
	
	public StreamedContent download(CobrancaUsuario cobranca) {   
		
		InfoCedente cedente = null;
		InfoSacado sacado = null;
		InfoEndereco endereco = null;
		Conta  conta = null;
		InfoTitulo titulo = null;
		
		try {
			cedente = conversorBoleto.criarCedente(usuario.getCondominio());
			sacado = conversorBoleto.criarSacado(cobranca.getUsuario());
			endereco = conversorBoleto.criarEndereco(usuario.getCondominio());
			sacado.setEndereco(endereco);
			ContaBancariaCondominio contaBancaria = condominioService.recuperarContaBancariaCondominioPrincipal(usuario.getCondominio().getId());
			conta = conversorBoleto.criarConta(contaBancaria);
			titulo = conversorBoleto.criarTitulo(cobranca);
		} catch (ErroConversaoException e1) {
			message.addError(e1.getMessage());
			return null;
		}
		
		if(StringUtils.isBlank(titulo.getNossoNumero())){
			NossoNumero nn = geradorNN.gerar(conta, titulo);
			//titulo.setNossoNumero(nn.getNumero());
			//titulo.setDigitoNossoNumero(nn.getDigito());
			
			titulo.setNossoNumero("000000012345");
			titulo.setDigitoNossoNumero("1");
		}
		
		File arquivoBoleto = fabrica.geraBoleto(cedente, conta, sacado, titulo);
		
		
		if(arquivoBoleto != null){
			InputStream stream;
			try {
				stream = new FileInputStream(arquivoBoleto);
				StreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "ExemploBoletoBB.pdf");
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		 }
		 return null;
    }


	
	
	public List<CobrancaUsuario> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<CobrancaUsuario> cobrancas) {
		this.cobrancas = cobrancas;
	}

	public List<String> getAnosMeses() {
		return anosMeses;
	}

	public void setAnosMeses(List<String> anosMeses) {
		this.anosMeses = anosMeses;
	}

	public String getAnoMes() {
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}
		
		
}

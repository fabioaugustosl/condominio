package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.jrimum.bopepo.Boleto;
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
import br.com.virtz.boleto.util.ZipUtil;
import br.com.virtz.condominio.boleto.conversor.ConversorDadosBoleto;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.ErroConversaoException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class DownloadBoletoController {

	
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
	
	private Usuario usuario = null;
	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
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
			titulo.setNossoNumero(nn.getNumero());
			titulo.setDigitoNossoNumero(nn.getDigito());
		}
		
		Boleto bol = fabrica.geraBoleto(cedente, conta, sacado, titulo);
		try {
			financeiroService.atualizarCobranca(cobranca.getId(), titulo.getNossoNumero(), titulo.getDigitoNossoNumero() , bol.getCodigoDeBarras().toString());
		} catch (AppException e1) {
			return null;
		}
		File arquivoBoleto = fabrica.boletoToFile(bol);
		
		if(arquivoBoleto != null){
			InputStream stream;
			try {
				stream = new FileInputStream(arquivoBoleto);
				StreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "boleto_"+cobranca.getAno()+cobranca.getMes()+".pdf");
				message.addInfo("Boleto gerado!");
				return file;
			} catch (FileNotFoundException e) {
				message.addError("Aconteceu um erro inesperado ao gerar o boleto.  "+ e.getMessage());
			}
		 }
		 return null;
    }
	
	
	
	public StreamedContent download(List<CobrancaUsuario> cobrancas) {  
		
		List<File> arquivos = new ArrayList<File>();
		
		if(cobrancas == null || cobrancas.isEmpty()){
			return null;
		}
		
		
		for(CobrancaUsuario cobranca : cobrancas){
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
				titulo.setNossoNumero(nn.getNumero());
				titulo.setDigitoNossoNumero(nn.getDigito());
			}
			
			Boleto bol = fabrica.geraBoleto(cedente, conta, sacado, titulo);
			try {
				financeiroService.atualizarCobranca(cobranca.getId(), titulo.getNossoNumero(),titulo.getDigitoNossoNumero() , bol.getCodigoDeBarras().write());
			} catch (AppException e1) {
				continue;
			}
			File arquivoBoleto = fabrica.boletoToFile(bol);
			arquivos.add(arquivoBoleto);
		}
		
		
		File arquivoBoleto = null;
		
		if(arquivos != null && !arquivos.isEmpty()){
			try {
				arquivoBoleto = new File("boletos.zip");
				ZipUtil.zipFile(arquivos, arquivoBoleto);
			}catch(Exception e){
				message.addError("Ocorreu um erro ao gerar os boletos.");
			}
		}
			
		if(arquivoBoleto != null){	
			InputStream stream;
			try {
				stream = new FileInputStream(arquivoBoleto);
				StreamedContent file = new DefaultStreamedContent(stream, "application/zip", "boletos.zip");
				message.addInfo("Boleto gerado!");
				return file;
			} catch (FileNotFoundException e) {
				message.addError("Aconteceu um erro inesperado ao gerar o boleto.  "+ e.getMessage());
			}
		 }
		 return null;
    }

		
}

package br.com.virtz.condominio.controller;

import java.io.File;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.jrimum.bopepo.Boleto;

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
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.ErroConversaoException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.util.MessageHelper;


public class BoletoController {

	protected @EJB IFinanceiroService financeiroService;	
	
	protected @EJB ICondominioService condominioService;
	
	protected @Inject ConversorDadosBoleto conversorBoleto;
	
	protected @Inject GeradorNossoNumero geradorNN;
	
	protected @Inject IFabricaBoletos fabrica;
	
	protected @Inject MessageHelper message;
	
	
	
	
	public BoletoController() {
		super();
	}

	public File gerar(CobrancaUsuario cobranca, Usuario usuario) {   
		
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
			financeiroService.atualizarCobranca(cobranca.getId(), titulo.getNossoNumero(), titulo.getDigitoNossoNumero() , bol.getCodigoDeBarras().write());
		} catch (AppException e1) {
			return null;
		}
		return fabrica.boletoToFile(bol);
	}

}
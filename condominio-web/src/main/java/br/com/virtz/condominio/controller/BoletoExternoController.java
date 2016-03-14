package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.AcessoCFTV;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.ArquivoTutorialCFTV;
import br.com.virtz.condominio.entidades.BoletoExterno;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;

@ManagedBean
@ViewScoped
public class BoletoExternoController {
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private BoletoExterno boleto = null;
	
	
	@PostConstruct
	public void init(){
		boleto = condominioService.recuperarBoletoExterno(sessao.getUsuarioLogado().getCondominio().getId());
	}
	
	
	public boolean possuiBoletoExterno(){
		if(boleto != null && boleto.getUrl() != null){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public String getImagem(){
		if(boleto.getLogomarca() != null){
			return "/arquivos/"+boleto.getLogomarca().getNome();
		}
		return null;
	}
	
	
	
	// GETTERS E SETTERS

	public BoletoExterno getBoleto() {
		return boleto;
	}

	public void setBoleto(BoletoExterno boleto) {
		this.boleto = boleto;
	}

}

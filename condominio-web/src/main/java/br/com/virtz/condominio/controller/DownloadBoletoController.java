package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.boleto.util.ZipUtil;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.session.SessaoUsuario;

@ManagedBean
@ViewScoped
public class DownloadBoletoController extends BoletoController {

	
	@Inject
	private SessaoUsuario sessao;
	
	Usuario usuario = null;
	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
	}
	

	
	
	public StreamedContent download(List<CobrancaUsuario> cobrancas) {  
		
		List<File> arquivos = new ArrayList<File>();
		
		if(cobrancas == null || cobrancas.isEmpty()){
			return null;
		}
		
		
		for(CobrancaUsuario cobranca : cobrancas){
			File arquivoBoleto = gerar(cobranca, usuario);
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
	
	
	
	public StreamedContent download(CobrancaUsuario cobranca) {   
		
	
		File arquivoBoleto = gerar(cobranca, usuario);
		
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

		
}

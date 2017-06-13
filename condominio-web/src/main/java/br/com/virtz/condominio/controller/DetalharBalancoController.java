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
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.ArquivoBalanco;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.Demonstrativo;
import br.com.virtz.condominio.service.IBalancoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class DetalharBalancoController {

	
	@EJB
	private IBalancoService balancoService;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	@Inject
	private NavigationPage navigation;
	
	
	private Usuario usuario = null;
	private Balanco balanco = null;
	private Demonstrativo demonstrativo = null;

	
	
	@PostConstruct
	public void init(){
		this.usuario = sessao.getUsuarioLogado();
		Object competenciaDetalhar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idBalanco");
		
		if(competenciaDetalhar != null){
			montarBalanco(Long.parseLong(competenciaDetalhar.toString()));
		}
	}
	
	
	public void montarBalanco(Long idBalanco){
		
		balanco = balancoService.recuperarBalanco(idBalanco);
		demonstrativo = new Demonstrativo(balanco);
		if(balanco != null){

			try {
				List<ItemBalanco> despesas = balancoService.recuperarDespesas(balanco);
				List<ItemBalanco>  receitas = balancoService.recuperarReceitas(balanco);
				demonstrativo.setDespesas(despesas);
				demonstrativo.setReceitas(receitas);
			} catch (AppException e) {
			}
			
		} 
	}
	
	
	 public StreamedContent download(ArquivoBalanco arquivo) {        
		 if(arquivo != null){
			InputStream stream;
			try {
				stream = new FileInputStream(new File(arquivoUtil.getCaminhoArquivosUpload()+arquivo.getNome()));
				StreamedContent file = new DefaultStreamedContent(stream, arquivoUtil.getMimetypeArquivo(arquivo.getExtensao()), arquivo.getNomeOriginal());
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		 }
		 return null;
    }
	 
	 

	
	public void irParaListagem(){
		navigation.redirectToPage("/balanco/listagemBalanco.faces");
	}

	
	
	/* GETTER E SETTERS*/
	
	public Balanco getBalanco() {
		return balanco;
	}

	public Demonstrativo getDemonstrativo() {
		return demonstrativo;
	}


}

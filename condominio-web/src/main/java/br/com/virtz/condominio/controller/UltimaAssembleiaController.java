package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class UltimaAssembleiaController {

	@EJB
	private IAssembleiaService assembleiaService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Assembleia assembleia = null;
	
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		assembleia = assembleiaService.recuperarUltimaAssembleiasRealizadas(usuario.getCondominio().getId());
	}
	
		
	public boolean possuiUltimaAssembleia(){
		return assembleia != null;
	}
	

	public StreamedContent download() {
		ArquivoAtaAssembleia arquivo = assembleia.getArquivoAta();
		 if(arquivo != null){
			InputStream stream;
			try {
				stream = new FileInputStream(new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivo.getNome()));
				StreamedContent file = new DefaultStreamedContent(stream, arquivoUtil.getMimetypeArquivo(arquivo.getExtensao()), arquivo.getNomeOriginal());
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		 }
		 return null;
	}
	
	
	public void irParaListagem() {
		navegacao.redirectToPage("/assembleia/listagemAssembleia.faces");
	}


	// GETTERS E SETTERS
	public Assembleia getAssembleia() {
		return assembleia;
	}

	public void setAssembleia(Assembleia assembleia) {
		this.assembleia = assembleia;
	}
	
}

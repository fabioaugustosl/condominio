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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.Documento;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IDocumentoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemDocumentoController {

	@EJB
	private IDocumentoService documentoService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private List<Documento> documentos;
	
	@PostConstruct
	public void init(){
		documentos = listarTodos(); 
	}
	
	
	public List<Documento> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<Documento> documentos = documentoService.recuperarTodos(usuario.getCondominio());
		
		return documentos;
	}
	
	public void irParaCadastro(){
		navegacao.redirectToPage("/documento/cadastrarDocumento.faces");
	}
	
	
	 public StreamedContent download(ArquivoDocumento arquivo) {        
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
	 
	
	 public void removerDocumento(Documento doc) throws CondominioException {

		 Documento documento = documentoService.recuperar(doc.getId());
		 
		 String nomeArquivo = documento.getArquivo().getNome();
		 
		 documentoService.remover(doc.getId());
		 
		 messageHelper.addInfo("Documento removido com sucesso!");

		 File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+nomeArquivo);
		 arquivoDeletar.delete();
	 }
	 
	 
	 public void editar(Documento documento){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idDocumento", documento.getId());
		irParaCadastro();
	 }	


	// GETTERS E SETTERS
	public List<Documento> getDocumentos() {
		return documentos;
	}
	
	
	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}
	
		
}

package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.Documento;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IDocumentoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroDocumentoController {
	
	@EJB
	private IDocumentoService documentoService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Documento documento;
	private UploadedFile arquivo;
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		Object documentoEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idDocumento");
		
		if(documentoEditar == null){
			criarNovoDocumento(usuario);
		} else {
			documento = documentoService.recuperar(Long.parseLong(documentoEditar.toString()));
		}
	}


	private void criarNovoDocumento(Usuario usuario) {
		documento = new Documento();
		documento.setCondominio(usuario.getCondominio());
		documento.setAtivo(Boolean.TRUE);
	}

	
	public void salvarDocumento(ActionEvent event) throws CondominioException {
		if(documento == null || documento.getArquivo() == null){
			throw new CondominioException("Nenhum documento encontrado para ser salva.");
		}
		
		documento.setData(new Date());
		
		try{
			documentoService.salvar(documento);
			message.addInfo("O documento foi salvo com sucesso.");
			documento = null;
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a novo documento. Favor tente novamente.");
		}
	}
	
	
	public void removerArquivo(ActionEvent event) throws CondominioException {
		if(documento != null && documento.getArquivo() != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+documento.getArquivo().getNome());
			arquivoDeletar.delete();
			documento.setArquivo(null);
			message.addInfo("Arquivo removido com sucesso!");
		}
	}
	
		
	public void uploadArquivo(FileUploadEvent event) {
        try {
			InputStream inputStream = event.getFile().getInputstream();
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = event.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_DOCUMENTO);
			
			ArquivoDocumento arqDocumento = new ArquivoDocumento();
			arqDocumento.setCaminho(caminho);
			arqDocumento.setExtensao(extensao);
			arqDocumento.setNomeOriginal(nomeAntigo);
			arqDocumento.setTamanho(event.getFile().getSize());
			arqDocumento.setNome(nomeNovo);
			
			arquivoUtil.arquivar(inputStream, nomeNovo);
			
			documento.setArquivo(arqDocumento);
			
			message.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
            message.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
    }
	
	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void irParaListagem(){
		navegacao.redirectToPage("/documento/listagemDocumento.faces");
	}


	
	/* GETTERS E SETTERS*/
		
	public Documento getDocumento() {
		return documento;
	}
	
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	
}


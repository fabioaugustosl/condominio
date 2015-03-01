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

import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.Documento;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IDocumentoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarAssembleiaController {
	
	@EJB
	private IAssembleiaService assembleiaService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Assembleia assembleia;
	private UploadedFile arquivo;
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		Object assembleiaEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idAssembleia");
		
		if(assembleiaEditar == null){
			criarNovaAssembleia(usuario);
		} else {
			assembleia = assembleiaService.recuperar(Long.parseLong(assembleiaEditar.toString()));
		}
	}


	private void criarNovaAssembleia(Usuario usuario) {
		assembleia = new Assembleia();
		assembleia.setCondominio(usuario.getCondominio());
		assembleia.setDataCadastro(new Date());
	}

	
	public void salvarAssembleia(ActionEvent event) throws CondominioException {
		if(assembleia == null){
			throw new CondominioException("Nenhuma assembléia encontrada para ser salva.");
		}
				
		try{
			assembleiaService.salvar(assembleia);
			message.addInfo("A assembléia foi salva com sucesso.");
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a nova assembléia. Favor tente novamente.");
		}
	}
	
	
	public void removerArquivo(ActionEvent event) throws CondominioException {
		if(assembleia != null && assembleia.getArquivoAta() != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+assembleia.getArquivoAta().getNome());
			arquivoDeletar.delete();
			assembleia.setArquivoAta(null);
			message.addInfo("Ata removida com sucesso!");
		}
	}
	
		
	public void uploadArquivo(FileUploadEvent event) {
        try {
			InputStream inputStream = event.getFile().getInputstream();
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = event.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_ATA);
			
			ArquivoAtaAssembleia arqDocumento = new ArquivoAtaAssembleia();
			arqDocumento.setCaminho(caminho);
			arqDocumento.setExtensao(extensao);
			arqDocumento.setNomeOriginal(nomeAntigo);
			arqDocumento.setTamanho(event.getFile().getSize());
			arqDocumento.setNome(nomeNovo);
			
			arquivoUtil.arquivar(inputStream, nomeNovo);
			
			assembleia.setArquivoAta(arqDocumento);
			
			message.addInfo("Ata "+nomeAntigo+" foi anexada com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
/*	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }*/
	
	public void irParaListagem(){
		navegacao.redirectToPage("/assembleia/listagemAssembleia.faces");
	}


	
	/* GETTERS E SETTERS*/
		
	public UploadedFile getArquivo() {
		return arquivo;
	}

	public Assembleia getAssembleia() {
		return assembleia;
	}

	public void setAssembleia(Assembleia assembleia) {
		this.assembleia = assembleia;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	
}


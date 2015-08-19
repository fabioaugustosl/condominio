package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IAssembleiaService;
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
	
	private boolean possoCadastrarPauta;
	private boolean possoCadastrarAta;
	private String novaPauta = null;
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		Object assembleiaEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idAssembleia");
		
		if(assembleiaEditar == null){
			criarNovaAssembleia(usuario);
			possoCadastrarPauta = false;
			possoCadastrarAta = false;
		} else {
			assembleia = assembleiaService.recuperar(Long.parseLong(assembleiaEditar.toString()));
			if(assembleia.getArquivoAta() == null){
				assembleia.setArquivoAta(new ArquivoAtaAssembleia());
			}
			possoCadastrarPauta = true;
			possoCadastrarAta = true;
		}
	}


	private void criarNovaAssembleia(Usuario usuario) {
		assembleia = new Assembleia();
		assembleia.setCondominio(usuario.getCondominio());
		assembleia.setDataCadastro(new Date());
		assembleia.setPermitirPautas(Boolean.TRUE);
	}

	
	public void salvarAssembleia(ActionEvent event) throws CondominioException {
		if(assembleia == null){
			throw new CondominioException("Nenhuma assembléia encontrada para ser salva.");
		}
				
		try{
			assembleiaService.salvar(assembleia);
			message.addInfo("A assembléia foi salva com sucesso.");
			possoCadastrarPauta = true;
			possoCadastrarAta = true;
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a nova assembléia. Favor tente novamente.");
		}
	}
	
	
	public void salvarPauta(ActionEvent event) throws CondominioException {
		if(StringUtils.isNotBlank(novaPauta)){
					
			try{
				PautaAssembleia p = new PautaAssembleia();
				p.setAprovada(false);
				p.setMensagem(novaPauta);
				p.setUsuario(usuario);
				p.setAssembleia(getAssembleia());
				
				if(assembleia.getPautas() == null){
					assembleia.setPautas(new ArrayList<PautaAssembleia>());
				}
				
				assembleia.getPautas().add(p);
				
				assembleiaService.salvar(assembleia);
				assembleia = assembleiaService.recuperar(assembleia.getId());
				message.addInfo("Pauta inserida com sucesso.");
				novaPauta = null;
			}catch(Exception e){
				e.printStackTrace();
				throw new CondominioException("Ocorreu um erro inesperado ao salvar a nova pauta. Favor tente novamente.");
			}
		}
	}
	
	
	public void reprovarPauta(Long idPauta) throws CondominioException {
		if(idPauta != null){
					
			try{
				for(PautaAssembleia p : assembleia.getPautas()){
					if(p.getId().equals(idPauta)){
						p.setAprovada(Boolean.FALSE);
						break;
					}
				}
					
				assembleiaService.salvar(assembleia);
				message.addInfo("Pauta reprovada.");
			}catch(Exception e){
				e.printStackTrace();
				throw new CondominioException("Ocorreu um erro inesperado ao reprovar a nova pauta. Favor tente novamente.");
			}
		}
	}
	
	
	public void aprovarPauta(Long idPauta) throws CondominioException {
		if(idPauta != null){
					
			try{
				for(PautaAssembleia p : assembleia.getPautas()){
					if(p.getId().equals(idPauta)){
						p.setAprovada(Boolean.TRUE);
						break;
					}
				}
					
				assembleiaService.salvar(assembleia);
				message.addInfo("Pauta aprovada.");
			}catch(Exception e){
				e.printStackTrace();
				throw new CondominioException("Ocorreu um erro inesperado ao aprovar a nova pauta. Favor tente novamente.");
			}
		}
	}
	
	
	public void removerPauta(PautaAssembleia pauta) throws CondominioException {
		if(pauta != null){
					
			try{
				assembleia.getPautas().remove(pauta);
				assembleiaService.removerPauta(pauta.getId());
				message.addInfo("Pauta removida com sucesso.");
				novaPauta = null;
			}catch(Exception e){
				e.printStackTrace();
				throw new CondominioException("Ocorreu um erro inesperado ao remover a pauta. Favor tente novamente.");
			}
		}
	}
	
	public void removerArquivo(ActionEvent event) throws CondominioException {
		if(assembleia != null && assembleia.getArquivoAta() != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+assembleia.getArquivoAta().getNome());
			arquivoDeletar.delete();
			assembleia.setArquivoAta(null);
			try {
				assembleia = assembleiaService.salvar(assembleia);
			} catch (ErroAoSalvar e) {
				throw new CondominioException("Ocorreu um erro ao salvar a ata.");
			}
			message.addInfo("Ata removida com sucesso!");
		}
	}
	
		
	public void uploadArquivo(FileUploadEvent event) throws AppException {
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
			
			try {
				assembleia = assembleiaService.salvar(assembleia);
			} catch (ErroAoSalvar e) {
				throw new AppException("Ocorreu um erro ao salvar a ata.");
			}
			
			message.addInfo("Ata '"+nomeAntigo+"' foi anexada com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("Ocorreu inespedado ao salvar o arquivo da ata.");
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

	public boolean isPossoCadastrarPauta() {
		return possoCadastrarPauta;
	}

	public void setPossoCadastrarPauta(boolean possoCadastrarPauta) {
		this.possoCadastrarPauta = possoCadastrarPauta;
	}

	public String getNovaPauta() {
		return novaPauta;
	}

	public void setNovaPauta(String novaPauta) {
		this.novaPauta = novaPauta;
	}

	public boolean isPossoCadastrarAta() {
		return possoCadastrarAta;
	}

	public void setPossoCadastrarAta(boolean possoCadastrarAta) {
		this.possoCadastrarAta = possoCadastrarAta;
	}
	
	
}


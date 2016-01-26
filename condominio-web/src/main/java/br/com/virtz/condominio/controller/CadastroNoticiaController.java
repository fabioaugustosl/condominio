package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.INoticiaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroNoticiaController {
	
	@EJB
	private INoticiaService noticiaService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	@Inject
	private EnviarEmailSuporteController emailSuporte;
	
	
	private Noticia noticia;
	private UploadedFile arquivo;
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		Object noticiaEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idNoticia");
		

		if(noticiaEditar == null){
			criarNovaNoticia(usuario);
		} else {
			noticia = noticiaService.recuperarNoticia(Long.parseLong(noticiaEditar.toString()));
		}
	}


	private void criarNovaNoticia(Usuario usuario) {
		noticia = new Noticia();
		noticia.setCondominio(usuario.getCondominio());
		noticia.setAtiva(true);
	}

	
	public void salvarNoticia(ActionEvent event) throws CondominioException {
		if(noticia == null){
			throw new CondominioException("Nenhuma notícia encontrada para ser salva.");
		}
		
		noticia.setData(new Date());
		
		try{
			noticiaService.salvarNoticia(noticia);
			criarNovaNoticia(usuario);
			message.addInfo("A Notícia foi salva com sucesso.");
		}catch(Exception e){
			e.printStackTrace();
			try{
				emailSuporte.enviarEmail("Ocorreu um erro inesperado ao salvar a notícia.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a notícia. Favor tente novamente.");
		}
	}
	
	
	public void removerArquivo(ArquivoNoticia arquivo) throws CondominioException {
		if(arquivo != null){
			try{
				noticia.getArquivos().remove(arquivo);
				if(arquivo.getId() != null){
					noticiaService.removerArquivo(arquivo.getId());
				}
				File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivo.getNome());
				arquivoDeletar.delete();
				message.addInfo("Arquivo removido com sucesso!");
			}catch(Exception e){
				message.addError("Ocorreu um erro ao remover o arquivo da notícia. Foi enviado um email para o suporte técnico. Em breve o problema será corrigido.");
				try{
					emailSuporte.enviarEmail("Ocorreu um erro ao remover o arquivo da notícia.", e.getMessage(), usuario.getCondominio().getId());
				}catch(Exception e1){
				}
			}
		}
	}
	
		
	public void uploadArquivo(FileUploadEvent event) {
        try {
			InputStream inputStream = event.getFile().getInputstream();
			
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = event.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_NOTICIA);

			
			ArquivoNoticia arqNoticia = new ArquivoNoticia();
			arqNoticia.setCaminho(caminho);
			arqNoticia.setExtensao(extensao);
			arqNoticia.setNomeOriginal(nomeAntigo);
			arqNoticia.setTamanho(event.getFile().getSize());
			arqNoticia.setNome(nomeNovo);
			arqNoticia.setNoticia(noticia);
			
			//arquivoUtil.arquivar(inputStream, nomeNovo);
			arquivoUtil.redimensionarImagem(inputStream, arquivoUtil.getCaminhoArquivosUpload(), nomeNovo, extensao, 1000, 1000);
			
			noticia.getArquivos().add(arqNoticia);
			
			message.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
            try{
				emailSuporte.enviarEmail("Ocorreu um erro upload de imagem na notícia.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
        }
    }
	

	public void irParaListagem(){
		navegacao.redirectToPage("/noticia/listagemNoticia.faces");
	}
	
	public String getCaminhoApp(ArquivoNoticia arquivo){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/"+arquivo.getNome();
	}


	
	/* GETTERS E SETTERS*/
		
	
	public UploadedFile getArquivo() {
		return arquivo;
	}

	public Noticia getNoticia() {
		return noticia;
	}


	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}


	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	
}


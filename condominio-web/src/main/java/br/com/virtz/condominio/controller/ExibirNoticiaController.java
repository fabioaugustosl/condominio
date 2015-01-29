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

import com.sun.mail.imap.protocol.ListInfo;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Documento;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.INoticiaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ExibirNoticiaController {
	
	@EJB
	private INoticiaService noticiaService;

	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	public Noticia noticia;
	public List<Noticia> noticias;
	public List<ArquivoNoticia> imagensNoticia;
	public List<ArquivoNoticia> arquivosDownload;
	
	
	@PostConstruct
	public void init(){
		noticia = null;
		noticias = new ArrayList<Noticia>();
		
		Usuario usuario = sessao.getUsuarioLogado();
		Object noticiaExibir = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idNoticia");
		

		if(noticiaExibir != null){
			noticia = noticiaService.recuperarNoticia(Long.parseLong(noticiaExibir.toString()));
			imagensNoticia = montarImagensNoticia();
			arquivosDownload = montarArquivosDownloadNoticia();
		}
		
		recuperarNoticiasExibicao(usuario); 
	}
	
	
	public void recuperarNoticiasExibicao(Usuario usuario){
		noticias = noticiaService.recuperarNoticiasAtivas(usuario.getCondominio().getId());
	}
	
	public void preencherNoticiaDestaque(Noticia noticiaExibir){
		noticia = noticiaExibir;
		imagensNoticia = montarImagensNoticia();
		arquivosDownload = montarArquivosDownloadNoticia();
	}
	
	
	public List<ArquivoNoticia> getArquivosImagens(){
		 return imagensNoticia;
	}

	public List<ArquivoNoticia> getArquivosDownload() {
		return arquivosDownload;
	}


	public void setArquivosDownload(List<ArquivoNoticia> arquivosDownload) {
		this.arquivosDownload = arquivosDownload;
	}


	/**
	 * Percorre a lista de arquivos e separa as imagens.
	 * @return
	 */
	private List<ArquivoNoticia> montarImagensNoticia() {
		List<ArquivoNoticia> imagens = new ArrayList<ArquivoNoticia>();
		
		if(noticia.getArquivos() != null && !noticia.getArquivos().isEmpty()){
			
			for(ArquivoNoticia a : noticia.getArquivos()){
				if(arquivoUtil.ehImagem(a.getExtensao())){
					imagens.add(a);
				}
			}
		}
		
		return imagens;
	}
	
	/**
	 * Percorre a lista de arquivos da noticia e separa os arquivos para download (diferente de imagens)
	 * @return
	 */
	private List<ArquivoNoticia> montarArquivosDownloadNoticia() {
		List<ArquivoNoticia> arquivos = new ArrayList<ArquivoNoticia>();
		
		if(noticia.getArquivos() != null && !noticia.getArquivos().isEmpty()){
			
			for(ArquivoNoticia a : noticia.getArquivos()){
				if(!arquivoUtil.ehImagem(a.getExtensao())){
					arquivos.add(a);
				}
			}
		}
		
		return arquivos;
	}
	
	
	public boolean existeImagemDestaque(){
		if(noticia != null){
			ArquivoNoticia arq = noticia.getArquivoNoticiaDestaque();
			if(arq != null && arquivoUtil.ehImagem(arq.getExtensao())){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	 public StreamedContent download(ArquivoNoticia arquivo) {        
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
	
	
	
	/* GETTERS e SETTERS */
	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}
		
	
}

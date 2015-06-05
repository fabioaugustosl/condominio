package br.com.virtz.condominio.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
public class NoticiaController {
	
	@EJB
	private INoticiaService noticiaService;

	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Noticia noticiaDestaque;
	private Noticia noticiaSelecionada;
	private ArquivoNoticia imagemNoticiaDestaque = null;
	private List<Noticia> noticias;
	
	
	@PostConstruct
	public void init(){
		noticiaDestaque = null;
		noticias = new ArrayList<Noticia>();
		noticiaSelecionada = null;
		recuperarNoticiasExibicao(); 
	}
	
	
	public void recuperarNoticiasExibicao(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		noticias = noticiaService.recuperarNoticiasAtivas(usuario.getCondominio().getId(), 4);
		if(noticias != null && !noticias.isEmpty()){
			noticiaDestaque = noticias.get(0);
			noticias.remove(noticiaDestaque);
			montarImagemNoticia(noticiaDestaque);
		}
		
	}
	
	
	private void montarImagemNoticia(Noticia noticia) {
		imagemNoticiaDestaque = null;
		if(noticia.getArquivos() != null && !noticia.getArquivos().isEmpty()){
			for(ArquivoNoticia a : noticia.getArquivos()){
				if(arquivoUtil.ehImagem(a.getExtensao())){
					imagemNoticiaDestaque = a;
					break;
				}
			}
		}
	}
	
	
	public void visualizar(){
		if(noticiaDestaque != null){
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idNoticia", noticiaDestaque.getId());
			navegacao.redirectToPage("/noticia/exibirNoticia.faces");
		}
	}
	
	
	public void preencherNoticiaDestaque(Noticia noticiaDestaque) {
		this.noticiaDestaque = noticiaDestaque;
	}

	
	/* GETTERS e SETTERS */
	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	public Noticia getNoticiaDestaque() {
		return noticiaDestaque;
	}

	public void setNoticiaDestaque(Noticia noticiaDestaque) {
		this.noticiaDestaque = noticiaDestaque;
	}

	public Noticia getNoticiaSelecionada() {
		return noticiaSelecionada;
	}

	public void setNoticiaSelecionada(Noticia noticiaSelecionada) {
		this.noticiaSelecionada = noticiaSelecionada;
	}

	public ArquivoNoticia getImagemNoticiaDestaque() {
		return imagemNoticiaDestaque;
	}
	
		
	
}

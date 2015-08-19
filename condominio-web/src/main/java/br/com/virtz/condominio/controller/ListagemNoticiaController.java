package br.com.virtz.condominio.controller;

import java.io.File;
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
public class ListagemNoticiaController {
	
	@EJB
	private INoticiaService noticiaService;

	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper mensagem;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	public List<Noticia> noticias;
	
	Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		noticias = listarTodas(); 
	}
	
	
	public List<Noticia> listarTodas(){
		List<Noticia> noticias = noticiaService.recuperarNoticias(usuario.getCondominio().getId());
		return noticias;
	}
	
	
	public void irParaCadastro(){
		navegacao.redirectToPage("/noticia/cadastrarNoticia.faces");
	}
	
	
	public void editar(Noticia noticia){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idNoticia", noticia.getId());
		irParaCadastro();
	}	
	 
	
	public void removerNoticia(Noticia noticia) throws CondominioException {

		 Noticia noticiaFull = noticiaService.recuperarNoticia(noticia.getId());
		 List<ArquivoNoticia> arquivos = noticiaFull.getArquivos();
		 
		 noticias.remove(noticia);
		 noticiaService.remover(noticia.getId());
		 
		 mensagem.addInfo("Notícia removida com sucesso!");

		 if(arquivos != null && !arquivos.isEmpty()){
			 for(ArquivoNoticia arq : noticiaFull.getArquivos()){
				 File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arq.getNome());
				 arquivoDeletar.delete();
			 }
		 }
	}
	 
	 
	public boolean podeEditar(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}
	 
	
	public boolean podeExcluir(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}
	 
	

	
	/* GETTERS e SETTERS */
	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}
	
	
}

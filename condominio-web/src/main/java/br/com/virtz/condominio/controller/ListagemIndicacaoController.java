package br.com.virtz.condominio.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.ArquivoIndicacao;
import br.com.virtz.condominio.entidades.CategoriaServicoProduto;
import br.com.virtz.condominio.entidades.Indicacao;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IIndicacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemIndicacaoController {
	
	@EJB
	private IIndicacaoService indicacaoService;

	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper mensagem;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	private List<Indicacao> indicacoesTodas = null;
	private List<Indicacao> indicacoes = null;
	private List<CategoriaServicoProduto> categorias = null;
	private Usuario usuario = null;
	private Indicacao indicacaoDestaque = null;
	
    private TagCloudModel model = null;
    private CategoriaServicoProduto categoriaSelecionada = null;
    private Set<ArquivoIndicacao> arquivos = null;

	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		
		Object paramIndDestaque = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idIndicacaoDestaque");
		
		indicacoesTodas = listarTodas();
		if(paramIndDestaque != null){
			indicacaoDestaque  = indicacaoService.recuperarIndicacao(Long.valueOf(paramIndDestaque.toString()));
			if(indicacaoDestaque.getCategorias() != null && !indicacaoDestaque.getCategorias().isEmpty()){
				CategoriaServicoProduto cat =  indicacaoDestaque.getCategorias().iterator().next();
				indicacoes = indicacaoService.recuperarIndicacoesPorCategoria(usuario.getCondominio().getId(), cat.getId());
			} else {
				indicacoes = indicacoesTodas;
			}
		} else {
			indicacaoDestaque = null;
			indicacoes = indicacoesTodas;
		}
		
		categorias = indicacaoService.recuperarTodasCategoriasComQuantidade(usuario.getCondominio().getId());
		arquivos = null;
		
		model = new DefaultTagCloudModel();
		for(CategoriaServicoProduto c : categorias){
			model.addTag(new DefaultTagCloudItem(c.getNome(), c.getQuantidade().intValue()));
		}
		
	}
	
	
	public List<Indicacao> listarTodas(){
		
		List<Indicacao> indicacoes = indicacaoService.recuperarIndicacoes(usuario.getCondominio().getId());
		
		return indicacoes;
	}
	
	
	public void irParaCadastro(){
		navegacao.redirectToPage("/indicacao/cadastrarIndicacao.faces");
	}
	
	
	/*public void editar(Noticia noticia){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idIndicacao", noticia.getId());
		irParaCadastro();
	}	*/
	 
	
	public void removerIndicacao(Indicacao indicacao) throws CondominioException {

		 Indicacao indicacaoFull = indicacaoService.recuperarIndicacao(indicacao.getId());
		 Set<ArquivoIndicacao> arquivos = indicacaoFull.getArquivos();
		 
		 indicacoes.remove(indicacao);
		 
		 indicacaoService.remover(indicacao.getId());
		 
		 mensagem.addInfo("Indicação removida com sucesso!");

		 if(arquivos != null && !arquivos.isEmpty()){
			 for(ArquivoIndicacao arq : indicacaoFull.getArquivos()){
				 File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arq.getNome());
				 arquivoDeletar.delete();
				 
				 try{
					File arquivoDeletarThumb = new File(getCaminhoCompletoThumb(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arq.getNome()));
					arquivoDeletarThumb.delete();
				}catch(Exception e1){
				}
			 
			 }
		 }
	}
	 
	 
	public boolean podeEditar(Indicacao indicacao){
		 if( (indicacao !=null && usuario.equals(indicacao.getUsuario())) || EnumTipoUsuario.SINDICO.equals(usuario.getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}
	 
	
	public boolean podeExcluir(Indicacao indicacao){
		if( (indicacao !=null && usuario.equals(indicacao.getUsuario())) || EnumTipoUsuario.SINDICO.equals(usuario.getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	}

	
	public void selecionarCategoria(SelectEvent event) {
        TagCloudItem item = (TagCloudItem) event.getObject();
        categoriaSelecionada = getCategoriaByTag(item);
        indicacoes = new ArrayList<Indicacao>();
        if(categoriaSelecionada != null){
        	indicacoes = indicacaoService.recuperarIndicacoesPorCategoria(usuario.getCondominio().getId(), categoriaSelecionada.getId());
        } else {
        	indicacoes =indicacoesTodas;
        }
        indicacaoDestaque  = null;
	}

	public void selecionarCategoriaCombo() {
        indicacoes = new ArrayList<Indicacao>();
        if(categoriaSelecionada != null){
        	indicacoes = indicacaoService.recuperarIndicacoesPorCategoria(usuario.getCondominio().getId(), categoriaSelecionada.getId());
        }
	}
	
	public List<CategoriaServicoProduto> completarCategoria(String query) {
        List<CategoriaServicoProduto> categFiltradas = new ArrayList<CategoriaServicoProduto>();
        
        if(StringUtils.isBlank(query)){
        	return categorias;
        }
        
        for (int i = 0; i < categorias.size(); i++) {
        	CategoriaServicoProduto skin = categorias.get(i);
            if(skin.getNome().toLowerCase().startsWith(query.toLowerCase())) {
                categFiltradas.add(skin);
            }
        }
         
        return categFiltradas;
    }

	private CategoriaServicoProduto getCategoriaByTag(TagCloudItem item) {
		for(CategoriaServicoProduto c : categorias){
        	if(c.getNome().equals(item.getLabel())){
        		return c;
        	}
        }
		return null;
	}
	
	
	public void selecionarIndicacao(Indicacao indicacao) {
        arquivos = indicacao.getArquivos();
	}
	
	
	public String getCaminhoCompletoThumb(String nomeArq){
		String ext = "."+arquivoUtil.pegarExtensao(nomeArq);
		String novoNome = nomeArq.replace(ext, "");
		return novoNome+ArquivosUtil.THUMB_POS_FIXO+ext;
	}
	
	/* GETTERS e SETTERS */
	
	public List<Indicacao> getIndicacoes() {
		return indicacoes;
	}

	public void setIndicacoes(List<Indicacao> indicacoes) {
		this.indicacoes = indicacoes;
	}

	public List<CategoriaServicoProduto> getCategorias() {
		return categorias;
	}

	public TagCloudModel getModel() {
		return model;
	}

	public CategoriaServicoProduto getCategoriaSelecionada() {
		return categoriaSelecionada;
	}

	public void setCategoriaSelecionada(CategoriaServicoProduto categoriaSelecionada) {
		this.categoriaSelecionada = categoriaSelecionada;
	}

	public Set<ArquivoIndicacao> getArquivos() {
		return arquivos;
	}

	public void setArquivos(Set<ArquivoIndicacao> arquivos) {
		this.arquivos = arquivos;
	}

	public Indicacao getIndicacaoDestaque() {
		return indicacaoDestaque;
	}
	
			 
}

package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.virtz.condominio.entidades.ArquivoIndicacao;
import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.CategoriaServicoProduto;
import br.com.virtz.condominio.entidades.Indicacao;
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
public class CadastrarIndicacaoController {
	
	@EJB
	private IIndicacaoService indicacaoService;
	
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
	
	
	private Indicacao indicacao = null;
	private UploadedFile arquivo = null;
	private Usuario usuario = null;
	
	private List<CategoriaServicoProduto> todasCategorias = null;
	private List<CategoriaServicoProduto> categoriasSelecionadas = null;
	private CategoriaServicoProduto categoriaSelecionada = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		Object indicacaoEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idIndicao");
		
		categoriasSelecionadas = new ArrayList<CategoriaServicoProduto>();
		categoriaSelecionada = null;
		todasCategorias = indicacaoService.recuperarTodasCategorias();

		if(indicacaoEditar == null){
			criarNovaIndicacao(usuario);
		} else {
			indicacao = indicacaoService.recuperarIndicacao(Long.parseLong(indicacaoEditar.toString()));
		}
	}


	private void criarNovaIndicacao(Usuario usuario) {
		indicacao = new Indicacao();
		indicacao.setCondominio(usuario.getCondominio());
		indicacao.setUsuario(usuario);
	}

	
	public void salvarIndicacao(ActionEvent event) throws CondominioException {
		if(indicacao == null){
			throw new CondominioException("Nenhuma indicação encontrada para ser salva.");
		}
		
		indicacao.setData(new Date());
		try{
			/*if(categoriasSelecionadas != null && !categoriasSelecionadas.isEmpty()){
				indicacao.setCategorias(new HashSet<CategoriaServicoProduto>(categoriasSelecionadas));
			}*/
			if(categoriaSelecionada != null){
				indicacao.setCategorias(new HashSet<CategoriaServicoProduto>());
				indicacao.getCategorias().add(categoriaSelecionada);
			}
			indicacaoService.salvarIndicacao(indicacao);
			criarNovaIndicacao(usuario);
			categoriasSelecionadas.clear();
			categoriaSelecionada = null;
			message.addInfo("Sua indicação foi salva com sucesso.");
		}catch(Exception e){
			e.printStackTrace();
			try{
				emailSuporte.enviarEmail("Ocorreu um erro inesperado ao salvar uma indicação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
			throw new CondominioException("Ocorreu um erro inesperado ao salvar a indicação. Favor tente novamente.");
		}
	}
	
	
	public void removerArquivo(ArquivoIndicacao arquivo) throws CondominioException {
		if(arquivo != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivo.getNome());
			arquivoDeletar.delete();
			
			try{
				File arquivoDeletarThumb = new File(getCaminhoCompletoThumb(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivo.getNome()));
				arquivoDeletarThumb.delete();
			}catch(Exception e1){
			}
			
			indicacao.getArquivos().remove(arquivo);
			if(arquivo.getId() != null){
				indicacaoService.remover(arquivo.getId());
			}
			message.addInfo("Arquivo removido com sucesso!");
		}
	}
	
		
	public void uploadArquivo(FileUploadEvent event) {
        try {
			InputStream inputStream = event.getFile().getInputstream();
			//InputStream inputStreamThumb = event.getFile().getInputstream();
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = event.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_IMAGEM);
			
			ArquivoIndicacao arqIndicacao = new ArquivoIndicacao();
			arqIndicacao.setCaminho(caminho);
			arqIndicacao.setExtensao(extensao);
			arqIndicacao.setNomeOriginal(nomeAntigo);
			arqIndicacao.setTamanho(event.getFile().getSize());
			arqIndicacao.setNome(nomeNovo);
			arqIndicacao.setIndicacao(indicacao);
			
			//arquivoUtil.arquivar(inputStream, nomeNovo);
			
			arquivoUtil.redimensionarImagem(inputStream, arquivoUtil.getCaminhoArquivosUpload(), nomeNovo, extensao, 1000, 1000);
			
			if(indicacao.getArquivos() == null){
				indicacao.setArquivos(new HashSet<ArquivoIndicacao>());
			}
			indicacao.getArquivos().add(arqIndicacao);
			
			try{
				// cria um thumb
				//arquivoUtil.redimensionarImagem(inputStreamThumb, arquivoUtil.getCaminhoArquivosUpload(), this.getCaminhoCompletoThumb(nomeNovo), extensao, 50, 50);
				arquivoUtil.gravarThumb(nomeNovo);
			}catch(Exception e1){
				e1.printStackTrace();
			}
			
			message.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
            try{
				emailSuporte.enviarEmail("Ocorreu um erro inesperado ao fazer upload de foto na indicação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
        }
    }
	
	public String getCaminhoCompletoThumb(String nomeArq){
		String ext = "."+arquivoUtil.pegarExtensao(nomeArq);
		String novoNome = nomeArq.replace(ext, "");
		return novoNome+ArquivosUtil.THUMB_POS_FIXO+ext;
	}
	

	public void irParaListagem(){
		navegacao.redirectToPage("/indicacao/listagemIndicacao.faces");
	}
	
	public String getCaminhoApp(ArquivoNoticia arquivo){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/"+arquivo.getNome();
	}
	
	public List<CategoriaServicoProduto> completarCategoria(String query) {
        
        List<CategoriaServicoProduto> categoriasFiltradas = new ArrayList<CategoriaServicoProduto>();
         
        for (int i = 0; i < todasCategorias.size(); i++) {
        	CategoriaServicoProduto nomeCat = todasCategorias.get(i);
            if(nomeCat.getNome().toLowerCase().startsWith(query.toLowerCase())) {
                categoriasFiltradas.add(nomeCat);
            }
        }
         
        return categoriasFiltradas;
    }


	
	/* GETTERS E SETTERS*/
		
	
	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	public Indicacao getIndicacao() {
		return indicacao;
	}

	public void setIndicacao(Indicacao indicacao) {
		this.indicacao = indicacao;
	}

	public List<CategoriaServicoProduto> getCategoriasSelecionadas() {
		return categoriasSelecionadas;
	}

	public void setCategoriasSelecionadas(
			List<CategoriaServicoProduto> categoriasSelecionadas) {
		this.categoriasSelecionadas = categoriasSelecionadas;
	}

	public List<CategoriaServicoProduto> getTodasCategorias() {
		return todasCategorias;
	}

	public CategoriaServicoProduto getCategoriaSelecionada() {
		return categoriaSelecionada;
	}

	public void setCategoriaSelecionada(CategoriaServicoProduto categoriaSelecionada) {
		this.categoriaSelecionada = categoriaSelecionada;
	}
	
	
}


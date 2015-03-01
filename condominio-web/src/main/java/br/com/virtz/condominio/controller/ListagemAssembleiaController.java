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
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.Documento;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemAssembleiaController {

	@EJB
	private IAssembleiaService assembleiaService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private List<Assembleia> assembleias;
	private Assembleia assembleiaSelecionada;
	
	@PostConstruct
	public void init(){
		assembleias = listarTodos(); 
	}
	
	
	public List<Assembleia> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<Assembleia> lista =  assembleiaService.recuperarTodos(usuario.getCondominio().getId());
		
		return lista;
	}
	
	
	public void irParaCadastro(){
		navegacao.redirectToPage("/assembleia/cadastrarAssembleia.faces");
	}
	
	
	 public StreamedContent download(ArquivoAtaAssembleia arquivo) {        
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
	 
	
	 public void removerPauta(PautaAssembleia pauta) throws CondominioException {
		 assembleiaSelecionada.getPautas().remove(pauta);
		 assembleiaService.removerPauta(pauta.getId());
		 messageHelper.addInfo("Pauta removida com sucesso!");
		 
	 }
	 
	 public void removerAssembleia(Assembleia assembleia) throws CondominioException {

		 String nomeArquivo = null;
		 if(assembleia.getArquivoAta() != null){
			 nomeArquivo = assembleia.getArquivoAta().getNome();
		 }
		 assembleiaService.removerAssembleia(assembleia.getId());
		 
		 messageHelper.addInfo("Assembleia removida com sucesso!");

		 if(StringUtils.isNotBlank(nomeArquivo)){
			 File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+nomeArquivo);
			 arquivoDeletar.delete();
		 }
	 }
	 
	 
	 public void editar(Assembleia assembleia){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idAssembleia", assembleia.getId());
		irParaCadastro();
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


	 
	 // GETTERS E SETTERS
	 public List<Assembleia> getAssembleias() {
		return assembleias;
	 }

	 public void setAssembleias(List<Assembleia> assembleias) {
		this.assembleias = assembleias;
	 }

	 public Assembleia getAssembleiaSelecionada() {
		return assembleiaSelecionada;
	 }

	 public void setAssembleiaSelecionada(Assembleia assembleiaSelecionada) {
		this.assembleiaSelecionada = assembleiaSelecionada;
	 }
	 
		
}

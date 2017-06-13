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
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.controller.lazy.RegistroOcorrenciaLazyModel;
import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.ArquivoOcorrencia;
import br.com.virtz.condominio.entidades.RegistroOcorrencia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IRegistroOcorrenciaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemRegistroOcorrenciasController {

	@EJB
	private IRegistroOcorrenciaService registroService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;

	private LazyDataModel<RegistroOcorrencia> ocorrencias = null;
	private Usuario usuario = null;



	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
	}



	public LazyDataModel<RegistroOcorrencia> getOcorrencias(){
		if(ocorrencias == null){
			ocorrencias = new RegistroOcorrenciaLazyModel( registroService.recuperarPorCondominioPaginado(usuario.getCondominio().getId(), 0, 50), usuario.getCondominio().getId(),registroService);
		}

		return ocorrencias;
	}

	public List<RegistroOcorrencia> listarTodos(){
		List<RegistroOcorrencia> lista = registroService.recuperarTodos(usuario.getCondominio().getId());
		return lista;
	}


	public void irParaCadastro(){
		navegacao.redirectToPage("/ocorrencia/cadastrarRegistroOcorrencia.faces");
	}

	public StreamedContent download(RegistroOcorrencia ocorrencia) {
		ArquivoOcorrencia arquivo = ocorrencia.getArquivo();
		 if(arquivo != null){
			InputStream stream;
			try {
				stream = new FileInputStream(new File(arquivoUtil.getCaminhoArquivosUpload()+arquivo.getNome()));
				StreamedContent file = new DefaultStreamedContent(stream, arquivoUtil.getMimetypeArquivo(arquivo.getExtensao()), arquivo.getNomeOriginal());
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		 }
		 return null;
	}

	// GETTERS E SETTERS

}

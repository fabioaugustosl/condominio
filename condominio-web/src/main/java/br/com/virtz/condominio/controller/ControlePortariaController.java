package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.ArquivoNotificacaoPortaria;
import br.com.virtz.condominio.entidades.NotificacaoPortaria;
import br.com.virtz.condominio.service.INotificacaoPortariaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@RequestScoped
public class ControlePortariaController {

	@EJB
	private INotificacaoPortariaService notificacaoService;

	@Inject
	private NavigationPage navegacao;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private MessageHelper message;

	@Inject
	private IArquivosUtil arquivoUtil;


	private List<NotificacaoPortaria> notificacoes = null;

	@PostConstruct
	public void init(){
		notificacoes = notificacaoService.recuperarPorCondominio(sessao.getUsuarioLogado().getCondominio().getId());
	}


	public void irParaCadastroRecebido(){
		navegacao.redirectToPage("/portaria/cadastrarRecebido.faces");
	}


	public void irParaCadastroVisitante(){
		navegacao.redirectToPage("/portaria/cadastrarVisitante.faces");
	}




	public List<NotificacaoPortaria> getNotificacoes() {
		return notificacoes;
	}


	public StreamedContent download(NotificacaoPortaria notificacao) {
		ArquivoNotificacaoPortaria arquivo = notificacao.getArquivo();
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

}

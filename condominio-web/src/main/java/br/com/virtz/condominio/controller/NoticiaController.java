package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;

import br.com.virtz.condominio.controller.beanview.VotacaoView;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.ParametroObrigatorioNuloException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.INoticiaService;
import br.com.virtz.condominio.service.NoticiaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class NoticiaController {
	
	@EJB
	private INoticiaService noticiaService;

	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	public Noticia noticia;
	
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		noticia = noticiaService.criarNovaNoticia(usuario.getCondominio(), null, null);
	}
	

	public void uploadArquivo(FileUploadEvent event) {
        try {
            File targetFolder = new File("/arquivos/");
            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFolder, event.getFile().getFileName()));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	
	/* GETTERS e SETTERS */
	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}
	
}

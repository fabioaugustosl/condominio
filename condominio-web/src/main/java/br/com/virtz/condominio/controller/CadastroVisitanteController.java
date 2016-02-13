package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.event.CaptureEvent;

import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IVisitanteService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroVisitanteController {
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private IVisitanteService visitanteService;
	
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	

	private List<Bloco> blocos = null;
	private Bloco blocoSelecionado;
	private Apartamento apartamentoSelecionado;
	
	private Usuario usuario = null;
	
	private String descricao = null; 
	
	private Visitante visitante = null;
	
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
				
		blocoSelecionado = null;
		blocos = condominioService.recuperarTodosBlocosComApartamentos(usuario.getCondominio().getId());
		if(blocos != null && blocos.size() == 1){
			blocoSelecionado = blocos.get(0);
		}
		apartamentoSelecionado = null;
		
		visitante = new Visitante();
		 
	}
	
	
	public void salvar(ActionEvent event) throws CondominioException {
		try{
			visitante.setDataEntrada(new Date());
			visitante.setCondominio(usuario.getCondominio());
			visitante.setApartamento(apartamentoSelecionado);
			visitante.setComentario(descricao);
			visitanteService.salvarVisitante(visitante);
			descricao = null;
			apartamentoSelecionado = null;
			blocoSelecionado = null;
			visitante = new Visitante();
			message.addInfo("O visitante foi salvo com sucesso.");
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao registrar o visitante.");
		}
	}
	
	public void voltar(){
		navegacao.redirectToPage("/portaria/gerenciarPortaria.faces");
	}
	
	public void irParaListagem(){
		navegacao.redirectToPage("/portaria/listagemVisitantes.faces");
	}
	
	
	public void oncapture(CaptureEvent captureEvent) {
		String filename = arquivoUtil.gerarNomeAleatorio(ArquivosUtil.TIPO_PORTARIA);
        byte[] data = captureEvent.getData();
         
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = arquivoUtil.getCaminhoArquivosUpload() +  filename + ".jpg";
         
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            visitante.setFoto("/arquivos/"+filename+".jpg");
            message.addInfo("Imagem capturada com sucesso!");
        }
        catch(IOException e) {
        	e.printStackTrace();
        	 message.addError("Ocorreu um erro ao capturar imagem!");
        }
    }
	
	
	/* GETTERS e SETTERS*/

	public String getFileNameCompleto(){
		return visitante.getFoto();
	}
	public List<Bloco> getBlocos() {
		return blocos;
	}

	public void setBlocos(List<Bloco> blocos) {
		this.blocos = blocos;
	}

	public Bloco getBlocoSelecionado() {
		return blocoSelecionado;
	}

	public void setBlocoSelecionado(Bloco blocoSelecionado) {
		this.blocoSelecionado = blocoSelecionado;
	}

	public Apartamento getApartamentoSelecionado() {
		return apartamentoSelecionado;
	}

	public void setApartamentoSelecionado(Apartamento apartamentoSelecionado) {
		this.apartamentoSelecionado = apartamentoSelecionado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}
	
}

package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.SelectEvent;

import br.com.virtz.condominio.entidades.AgrupamentoUnidades;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
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
	private IUsuarioService usuarioService;

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

	@Inject
	private PrincipalController principalController;

	private List<Bloco> blocos = null;
	private List<AgrupamentoUnidades> agrupamentos = null;
	private List<Usuario> usuarios = null;
	private Usuario moradorSelecionado = null;
	private Bloco blocoSelecionado;
	private Apartamento apartamentoSelecionado;
	private AgrupamentoUnidades agrupamentoSelecionado;

	private Usuario usuario = null;

	private String descricao = null;

	private Visitante visitante = null;



	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();

		usuarios = listarTodos();
		
		blocoSelecionado = null;

		if(principalController.condominioPossuiAgrupamento()){
			recuperarAgrupamentos();
		} else{
			recuperarBlocos();
		}

		apartamentoSelecionado = null;
		moradorSelecionado = null;

		visitante = new Visitante();

	}
	
	
	public List<Usuario> listarTodos(){
		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		return usuarios;
	}

	
	public List<Usuario> pesquisarMorador(String query) {
		List<Usuario> usuarioFiltrados = new ArrayList<Usuario>();
		
		if(StringUtils.isBlank(query)){
			return usuarios;
		}
		
		for (int i = 0; i < usuarios.size(); i++) {
			 Usuario u = usuarios.get(i);
            if(u.getNome().toLowerCase().startsWith(query.toLowerCase())) {
                usuarioFiltrados.add(u);
            }
        }
	 	         
		return usuarioFiltrados;
	}
	 
	
	public void selecionarMorador(ValueChangeEvent event) {
		Usuario usuario = (Usuario) event.getNewValue();
		
		if(this.visitante != null){
			this.visitante.setApartamento(usuario.getApartamento());
		}
		
		blocoSelecionado = usuario.getApartamento().getBloco();
		apartamentoSelecionado = usuario.getApartamento();
		agrupamentoSelecionado = usuario.getApartamento().getBloco().getAgrupamentoUnidades();
    }
	
	 
	public void recuperarAgrupamentos() {
		agrupamentos = condominioService.recuperarTodosAgrupamentos(usuario.getCondominio().getId());
		if(agrupamentos != null && agrupamentos.size() == 1){
			agrupamentoSelecionado = agrupamentos.get(0);
		}
	}


	public void recuperarBlocos() {
		blocos = condominioService.recuperarTodosBlocosComApartamentos(usuario.getCondominio().getId());
		if(blocos != null && blocos.size() == 1){
			blocoSelecionado = blocos.get(0);
		}
	}

	public void recuperarBlocosPorAgrupamento() {
		if(agrupamentoSelecionado != null){
			blocos = condominioService.recuperarTodosBlocosPorAgrupamento(agrupamentoSelecionado.getId());
			if(blocos != null && blocos.size() == 1){
				blocoSelecionado = blocos.get(0);
			}
		}
	}



	public void salvar(ActionEvent event) throws CondominioException {
		try{
			if(apartamentoSelecionado == null && this.visitante.getApartamento() == null){
				message.addError("É necessário selecionar a unidade que o visitante vai acessar.");
				return;
			}
			
			visitante.setDataEntrada(new Date());
			visitante.setCondominio(usuario.getCondominio());
			if(visitante.getApartamento() == null){
				visitante.setApartamento(apartamentoSelecionado);
			}
			visitante.setComentario(descricao);
			
			visitanteService.salvarVisitante(visitante);
			
			descricao = null;
			apartamentoSelecionado = null;
			blocoSelecionado = null;
			moradorSelecionado = null;
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

	public AgrupamentoUnidades getAgrupamentoSelecionado() {
		return agrupamentoSelecionado;
	}

	public void setAgrupamentoSelecionado(AgrupamentoUnidades agrupamentoSelecionado) {
		this.agrupamentoSelecionado = agrupamentoSelecionado;
	}

	public List<AgrupamentoUnidades> getAgrupamentos() {
		return agrupamentos;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public Usuario getMoradorSelecionado() {
		return moradorSelecionado;
	}

	public void setMoradorSelecionado(Usuario moradorSelecionado) {
		this.moradorSelecionado = moradorSelecionado;
	}


}

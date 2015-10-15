package br.com.virtz.condominio.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class AtualizarUsuarioController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private CroppedImage imagemCortada = null;
    private String caminhoImagem = null;
    
	private Usuario usuario = null;
	private List<Bloco> blocos = null;
	private Bloco blocoSelecionado = null;
	private boolean trocouFoto;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		if(usuario.getArquivo() != null && !usuario.getArquivo().getNome().equals("foto.jpg")){
			this.caminhoImagem = usuario.getArquivo().getCaminhoCompleto();
		}
		if(usuario.getApartamento() != null){
			blocoSelecionado = condominioService.recuperarBloco(usuario.getApartamento().getBloco().getId());
		}else{
			blocoSelecionado = null;
		}
		
		blocos = condominioService.recuperarTodosBlocosComApartamentos(usuario.getCondominio().getId());
		
		trocouFoto = false;
	}

	
	public void cortar() throws CondominioException{
        if(imagemCortada != null && trocouFoto) {
	        ArquivoUsuario arquivo = createArquivo(Long.valueOf(imagemCortada.getBytes().length));
	    	arquivo.setAltura(imagemCortada.getHeight());
	    	arquivo.setLargura(imagemCortada.getWidth());
	        
	    	ByteArrayInputStream inputStream = new ByteArrayInputStream(imagemCortada.getBytes());
	    	try {
				arquivoUtil.arquivar(inputStream, arquivo.getNome());
				
				if(caminhoImagem != null){
					removerArquivo(caminhoImagem);
				}
			} catch (IOException e) {
				throw new CondominioException("Ocorreu um erro ao recortar a foto.");
			}
	    	
	    	usuario.setArquivo(arquivo);
        } 
        
        try {
			salvar();
			this.caminhoImagem = usuario.getArquivo().getCaminhoCompleto();
			imagemCortada = null;
			trocouFoto = false;
			message.addInfo("Parabéns. Seu cadastro foi atualizado com sucesso.");
		} catch (AppException e) {
			throw new CondominioException("Aconteceu um erro ao salvar o usuário. Favor tentar novamente.");
		}
	}


	private void salvar() throws AppException {
		
        try {
        	if(usuario.getArquivo() != null && trocouFoto){
        		ArquivoUsuario arq = usuarioService.salvarArquivo(usuario.getArquivo());
        		usuario.setArquivo(arq);
        	}
        	usuario = usuarioService.salvar(usuario);

        } catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		}
	}
	
	
	 public void uploadArquivo(FileUploadEvent event) throws CondominioException{
        try {
        	
        	if(!arquivoUtil.tamanhoImagemEhValido(event.getFile().getInputstream(), 300, 375)){
        		throw new CondominioException("Erro. A imagem deve ter largura mínima de 300 e altura mínima de 375 pixels.");
        	}
            
        	ArquivoUsuario arquivo = createArquivo(event.getFile().getSize());
        	arquivoUtil.redimensionarImagem(event.getFile().getInputstream(), arquivoUtil.getCaminhoUploadArquivosTemporario(), arquivo.getNome(), arquivo.getExtensao(), 600, 750);
        	this.caminhoImagem = arquivo.getCaminhoCompleto();
        	
        	trocouFoto = true;
        } catch (IOException e) {
            throw new CondominioException("Ocorreu um erro ao realizar o upload da imagem.");
        }
	 }
	 
	 
	 public void removerArquivo(String arquivo) {
		if(arquivo != null){
			File arquivoDeletar = new File(arquivo);
			arquivoDeletar.delete();
		}
	 }	
	 
	 
	 private ArquivoUsuario createArquivo(long tamanho){
		 String caminho = arquivoUtil.getCaminhoArquivosUpload();
		 String extensao = arquivoUtil.pegarExtensao("jpg");
		 String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_IMAGEM);
	 	
	 	 ArquivoUsuario arqUsuario = new ArquivoUsuario();
	 	 arqUsuario.setCaminho(caminho);
	 	 arqUsuario.setExtensao(extensao);
	 	 arqUsuario.setTamanho(tamanho);
	 	 arqUsuario.setNome(nomeNovo);
	 	 
	 	 return arqUsuario;
	 }

	
	
	 /*  GETTERS e SETTERs	 */

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public CroppedImage getImagemCortada() {
		return imagemCortada;
	}

	public void setImagemCortada(CroppedImage imagemCortada) {
		this.imagemCortada = imagemCortada;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
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

	public boolean isTrocouFoto() {
		return trocouFoto;
	}
	
	
}

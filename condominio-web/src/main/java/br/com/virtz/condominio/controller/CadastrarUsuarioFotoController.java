package br.com.virtz.condominio.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarUsuarioFotoController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private ITokenService tokenService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@Inject
	private NavigationPage navegacao;
	
	
	private CroppedImage imagemCortada = null;
    private String caminhoImagem = null;
    
	private Usuario usuario = null;
	private Boolean cadastroFinalizado= null;
	
	@PostConstruct
	public void init(){
		Object usuarioEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idUsuario");
		cadastroFinalizado= false;
		usuario = usuarioService.recuperarUsuarioCompleto(Long.parseLong(usuarioEditar.toString()));
		usuario.setArquivo(new ArquivoUsuario());
	}


   
	public void salvar(ActionEvent actionEvent) throws AppException {
        salvar();
    }


	private void salvar() throws AppException {
		
        try {
        	if(usuario.getArquivo() != null){
        		ArquivoUsuario arq = usuarioService.salvarArquivo(usuario.getArquivo());
        		usuario.setArquivo(arq);
        	}
        	usuario = usuarioService.salvar(usuario);
        	
        	// gerar token
        	Token token = tokenService.novoToken(String.valueOf(usuario.getId())); 
        	
        	// enviar email confirmação
        	try{
        		enviarEmailConfirmacaoCadastro(token);
        	}catch(Exception e){
        		message.addInfo("Ocorreu uma falha ao enviar email de confirmação pra você!");
        	}

        	cadastroFinalizado = Boolean.TRUE;
        } catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		} finally{
			usuario = null;
		}
	}



	private void enviarEmailConfirmacaoCadastro(Token token) {
		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("nomeUsuairo", usuario.getNome());
		mapParametrosEmail.put("token", token.getToken());
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(caminho, EnumTemplates.CONFIRMACAO_USUARIO.getNomeArquivo(), mapParametrosEmail);
		Email email = new Email("fabioaugustosl@gmail.com", "fabioaugustosl@gmail.com", "teste Fabio", msg);
   	//	enviarEmail.enviar(email);
	}

	
	 public void cortar() throws CondominioException{
        if(imagemCortada != null) {
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
        } else {
        	usuario.setArquivo(null);
        }
        
        try {
			salvar();
		} catch (AppException e) {
			throw new CondominioException("Aconteceu um erro ao salvar o usuário. Favor tentar novamente.");
		}
	 }
	 
	
	 public void uploadArquivo(FileUploadEvent event) throws CondominioException{
        try {
        	
        	if(!arquivoUtil.tamanhoImagemEhValido(event.getFile().getInputstream(), 300, 375)){
        		throw new CondominioException("Erro. A imagem deve ter largura mínima de 300 e altura mínima de 375 pixels.");
        	}
            
        	ArquivoUsuario arquivo = createArquivo(event.getFile().getSize());
        	arquivoUtil.redimensionarImagem(event.getFile().getInputstream(), arquivo.getNome(), arquivo.getExtensao(), 600, 750);
        	this.caminhoImagem = arquivo.getCaminhoCompleto();
        } catch (IOException e) {
            throw new CondominioException("Ocorreu um erro ao realizar o upload da imagem.");
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
	 
	 
	 public void removerArquivo(String arquivo) {
		if(arquivo != null){
			File arquivoDeletar = new File(arquivo);
			arquivoDeletar.delete();
		}
	 }	
		
	
	
	/*  GETTERS e SETTERs	 */

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}

	public CroppedImage getImagemCortada() {
		return imagemCortada;
	}

	public void setImagemCortada(CroppedImage imagemCortada) {
		this.imagemCortada = imagemCortada;
	}

	public Boolean getCadastroFinalizado() {
		return cadastroFinalizado;
	}

	public void setCadastroFinalizado(Boolean cadastroFinalizado) {
		this.cadastroFinalizado = cadastroFinalizado;
	}
	
	
}

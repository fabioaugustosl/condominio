package br.com.virtz.condominio.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IPublicidadeService;
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
	
	@EJB
	private IPublicidadeService publicidadeService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
	
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
		if(usuario != null){
			usuario.setArquivo(new ArquivoUsuario());
		}
		
		leitor.setPublicidadeService(publicidadeService);
	}

	
	 public void redirecionarPaginaInicial() throws AppException{
		navegacao.redirectToPage("/login.faces");
	 }


   
	public void salvar(ActionEvent actionEvent) throws AppException {
        salvar();
    }


	private void salvar() throws AppException {
		
        try {
        	if(usuario.existeFotoParaUsuario() && usuario.getArquivo() != null){
        		ArquivoUsuario arq = usuarioService.salvarArquivo(usuario.getArquivo());
        		usuario.setArquivo(arq);
        	}
        	usuario.setAdm(false);
        	usuario = usuarioService.salvar(usuario);
        	
        	// gerar token
        	Token token = tokenService.novoToken(String.valueOf(usuario.getId())); 
        	
        	// enviar email confirmação
        	try{
        		enviarEmailConfirmacaoCadastro(usuario, token);
        	}catch(Exception e){
        		message.addInfo("Ocorreu uma falha ao enviar email de confirmação pra você!");
        	}
        	
        	try{
        		enviarEmailAvisoCadastro(usuario);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        
        	cadastroFinalizado = Boolean.TRUE;
        } catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		} 
	}



	private void enviarEmailConfirmacaoCadastro(Usuario usuario, Token token) {
		// recuperar url da aplicação
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		StringBuffer sb = origRequest.getRequestURL().delete(origRequest.getRequestURL().indexOf("cadastrarUsuarioFoto.faces"), origRequest.getRequestURL().toString().length()); 
		sb.append("confirmarCadastro.faces?token=").append(token.getToken());

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("nomeUsuario", usuario.getNome());
		mapParametrosEmail.put("link", sb.toString());
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(usuario.getCondominio().getId(), caminho, EnumTemplates.CONFIRMACAO_USUARIO.getNomeArquivo(), mapParametrosEmail);
		
		Email email = new Email(EnumTemplates.CONFIRMACAO_USUARIO.getDe(), usuario.getEmail(), EnumTemplates.CONFIRMACAO_USUARIO.getAssunto(), msg);
		enviarEmail.enviar(email);
	}

	
	private void enviarEmailAvisoCadastro(Usuario usuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome usuário : ").append(usuario.getNome()).append("<br />");
		sb.append("ID usuário : ").append(usuario.getId()).append("<br />");
		
		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("titulo", "Novo usuário do site acaba de se cadastrar");
		mapParametrosEmail.put("msg", sb.toString());
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msg = leitor.processarTemplate(usuario.getCondominio().getId(),caminho, EnumTemplates.PADRAO.getNomeArquivo(), mapParametrosEmail);
		
		Email email = new Email(EnumTemplates.PADRAO.getDe(), "contatovirtz@gmail.com", EnumTemplates.PADRAO.getAssunto(), msg);
		enviarEmail.enviar(email);
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
        		message.addError("Erro. A imagem deve ter largura mínima de 300 e altura mínima de 375 pixels.");
        		return;
        	}
            
        	ArquivoUsuario arquivo = createArquivo(event.getFile().getSize());
        	arquivoUtil.redimensionarImagem(event.getFile().getInputstream(), arquivoUtil.getCaminhoUploadArquivosTemporario(), arquivo.getNome(), arquivo.getExtensao(), 900, 900, 300, 375);
//        	arquivoUtil.copiarArquivos(arquivo.getCaminhoCompleto());
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

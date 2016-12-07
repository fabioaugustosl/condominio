package br.com.virtz.condominio.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoUsuario;
import br.com.virtz.condominio.entidades.Unidade;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastrarPorteiroController implements Serializable{

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

	@Inject
	private LeitorTemplate leitor;

	@Inject
	private NavigationPage navegacao;


	private Usuario usuario = null;
	private Usuario usuarioLogado = null;
	private CroppedImage imagemCortada = null;
    private String caminhoImagem = null;


	@PostConstruct
	public void init(){
		usuario = new Usuario();
		usuario.setArquivo(new ArquivoUsuario());
		usuario.setCadastroConfirmado(Boolean.TRUE);
		usuario.setTipoUsuario(EnumTipoUsuario.PORTEIRO);

		usuarioLogado = sessao.getUsuarioLogado();
	}


	private void salvar() throws AppException {
		if(usuario.getSenhaDigitada().length() < 6){
			throw new AppException("A senha deve possuir no mínimo 6 caracteres.");
		}

		// validar se o email já existe
		Usuario u = usuarioService.recuperarUsuario(usuario.getEmail());
		if(u != null){
			throw new AppException("O email digitado já está sendo utilizado por outro usuário. Favor escolha outro email.");
		}
		usuario.setDeletado(false);
		usuario.setCondominio(usuarioLogado.getCondominio());
		usuario.setAdm(true);
		List<Unidade> unidades = condominioService.recuperarTodasUnidades(usuarioLogado.getCondominio().getId());
		if(unidades != null && !unidades.isEmpty()){
			Unidade unidade = null;
			for(Unidade uni : unidades){
				if(!uni.getAdm()){
					unidade = uni;
					break;
				}
			}
			usuario.setUnidade(unidade);
		}
        try {
        	usuario = usuarioService.salvar(usuario);
        } catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar seu usuário. Favor tentar novamente.");
		}
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

			this.caminhoImagem = usuario.getArquivo().getCaminhoCompleto();
			imagemCortada = null;
			message.addInfo("Porteiro salvo com succeso.");
		}catch (AppException e) {
			message.addError(e.getMessage());
		} catch (Exception exc) {
			throw new CondominioException("Aconteceu um erro ao salvar o usuário. Favor tentar novamente.");
		}
	 }


	 public void uploadArquivo(FileUploadEvent event) throws CondominioException{
	        try {

	        	if(!arquivoUtil.tamanhoImagemEhValido(event.getFile().getInputstream(), 300, 375)){
	        		//throw new CondominioException("Erro. A imagem deve ter largura mínima de 300 e altura mínima de 375 pixels.");
	        		message.addError("A imagem é muito pequena! Ela deve ter largura mínima de 300 e altura mínima de 375 pixels.");
	        		return;
	        	}

	        	ArquivoUsuario arquivo = createArquivo(event.getFile().getSize());
	        	arquivoUtil.redimensionarImagem(event.getFile().getInputstream(), arquivoUtil.getCaminhoUploadArquivosTemporario(), arquivo.getNome(), arquivo.getExtensao(), 900, 900, 300, 375);
	        	this.caminhoImagem = arquivo.getCaminhoCompleto();
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


		 @SuppressWarnings("static-access")
			public void irParaListagem(){
				navegacao.redirectToPage("/portaria/listagemPorteiro.faces");
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


}

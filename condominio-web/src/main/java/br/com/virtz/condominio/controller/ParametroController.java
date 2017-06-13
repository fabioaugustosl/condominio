package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;

import br.com.virtz.boleto.util.ValidadorUtil;
import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.AcessoCFTV;
import br.com.virtz.condominio.entidades.ArquivoBoletoExterno;
import br.com.virtz.condominio.entidades.ArquivoTutorialCFTV;
import br.com.virtz.condominio.entidades.BoletoExterno;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IParametroSistemaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ParametroController {
	
	@EJB
	private IParametroSistemaService parametroService;
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private PrincipalController principalController;
	
	@Inject
	private MessageHelper mensagem;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private ArquivoTutorialCFTV tutorialCftv;
	private ArquivoBoletoExterno arquivoBoleto;
	private Usuario usuario = null;
	
	private ParametroSistema parametroMaximoDias = null;
	private ParametroSistema parametroMinimoDias = null;
	private ParametroSistema parametroEnviarEmailAta = null;
	private Boolean parametroEnviarEmailAtaBool = null;
	
	private AcessoCFTV cftv = null;
	private BoletoExterno boletoExterno = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		parametroMaximoDias = parametroService.recuperar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM, usuario.getCondominio());
		parametroMinimoDias = parametroService.recuperar(EnumParametroSistema.QUANTIDADE_DIAS_MINIMO_PARA_AGENDAR_AREA_COMUM, usuario.getCondominio());
		parametroEnviarEmailAta = parametroService.recuperar(EnumParametroSistema.AVISAR_POR_EMAIL_QUANDO_AXEXAR_ATA, usuario.getCondominio());
		parametroEnviarEmailAtaBool = ("1".equals(parametroEnviarEmailAta.getValor()) ? Boolean.TRUE: Boolean.FALSE);
		
		// cftv
		cftv = condominioService.recuperarCFTV(usuario.getCondominio().getId());
		if(cftv == null){
			cftv = new AcessoCFTV();
		} else {
			tutorialCftv = cftv.getArquivo();
		}
		
		// boleto externo
		boletoExterno = condominioService.recuperarBoletoExterno(usuario.getCondominio().getId());
		if(boletoExterno == null){
			boletoExterno = new BoletoExterno();
		} else {
			arquivoBoleto = boletoExterno.getLogomarca();
		}
		
	}
	
	
	public void salvar() throws AppException{
		
		try {
			parametroService.salvar(parametroMaximoDias);
			parametroService.salvar(parametroMinimoDias);
			
			parametroEnviarEmailAta.setValor(parametroEnviarEmailAtaBool ? "1" : "0");
			parametroService.salvar(parametroEnviarEmailAta);
			
			mensagem.addInfo("Configurações atualizadas com sucesso.");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o(s) bloco(s). Favor acesse o menu novamente e repita o processo.");
		}

	}

	
	public void salvarCFTV() throws AppException{
		
		try {
			if(cftv.getCondominio() == null){
				cftv.setCondominio(usuario.getCondominio());
			}
			
			if(tutorialCftv != null){
				cftv.setArquivo(tutorialCftv);
			}
			
			cftv = condominioService.salvarAcessoCFTV(cftv);
			
			// atualiza variavel da sessão
			principalController.getCondominio().setCftv(cftv);
			
			mensagem.addInfo("Configurações de acesso a sua CFTV foram atualizadas com sucesso.");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar dados de acesso a sua CFTV.");
		}

	}
	
	
	public void uploadArquivo(FileUploadEvent evento) throws CondominioException {
        try {
        	InputStream inputStream = evento.getFile().getInputstream();
		
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = evento.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_DOCUMENTO);
			
			tutorialCftv = new ArquivoTutorialCFTV();
			tutorialCftv.setCaminho(caminho);
			tutorialCftv.setExtensao(extensao);
			tutorialCftv.setNomeOriginal(nomeAntigo);
			tutorialCftv.setTamanho(evento.getFile().getSize());
			tutorialCftv.setNome(nomeNovo);
			
			arquivoUtil.arquivar(inputStream, nomeNovo);
					
			mensagem.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
        	mensagem.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
    }
	
	public void removerTutorial() throws CondominioException {
		if(tutorialCftv != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+tutorialCftv.getNome());
			arquivoDeletar.delete();
			
			tutorialCftv = null;
			
			mensagem.addInfo("Arquivo removido com sucesso!");
		}
	}
	
	
	

	public void salvarBoletoExterno() throws AppException{
		
		try {
			
			if(boletoExterno.getId()!=null && boletoExterno.getDescricaoLink() == null && boletoExterno.getUrl() == null){
				condominioService.removerBoletoExterno(boletoExterno.getId());
				return;
			}
			
			if(boletoExterno.getCondominio() == null){
				boletoExterno.setCondominio(usuario.getCondominio());
			}
			
			if(arquivoBoleto != null){
				boletoExterno.setLogomarca(arquivoBoleto);
			}
			
			ValidadorUtil validador = new ValidadorUtil();
			if(!validador.validateEmail(boletoExterno.getUrl()) && 
					!(boletoExterno.getUrl().toLowerCase().startsWith("http://") || boletoExterno.getUrl().toLowerCase().startsWith("https://"))){
				boletoExterno.setUrl("http://"+boletoExterno.getUrl());
			}
			
			boletoExterno = condominioService.salvarBoletoExterno(boletoExterno);
			
			mensagem.addInfo("Configurações de acesso ao link do boleto externo foram atualizadas com sucesso.");
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar dados de acesso ao boleto externo.");
		}

	}
	
	
	public void uploadArquivoBoleto(FileUploadEvent evento) throws CondominioException {
        try {
        	InputStream inputStream = evento.getFile().getInputstream();
		
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = evento.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_IMAGEM);
			
			arquivoBoleto = new ArquivoBoletoExterno();
			arquivoBoleto.setCaminho(caminho);
			arquivoBoleto.setExtensao(extensao);
			arquivoBoleto.setNomeOriginal(nomeAntigo);
			arquivoBoleto.setTamanho(evento.getFile().getSize());
			arquivoBoleto.setNome(nomeNovo);
			
			arquivoUtil.redimensionarImagem(inputStream, caminho, nomeNovo, extensao, 230, 200);
					
			mensagem.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
        	mensagem.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
    }
	
	public void removerLogomarca() throws CondominioException {
		if(arquivoBoleto != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivoBoleto.getNome());
			arquivoDeletar.delete();
			
			arquivoBoleto = null;
			
			mensagem.addInfo("Arquivo removido com sucesso!");
		}
	}
	
	

	// GETTERS E SETTERS
	public ParametroSistema getParametroMaximoDias() {
		return parametroMaximoDias;
	}

	public void setParametroMaximoDias(ParametroSistema parametroMaximoDias) {
		this.parametroMaximoDias = parametroMaximoDias;
	}

	public Boolean getParametroEnviarEmailAtaBool() {
		return parametroEnviarEmailAtaBool;
	}

	public void setParametroEnviarEmailAtaBool(Boolean parametroEnviarEmailAtaBool) {
		this.parametroEnviarEmailAtaBool = parametroEnviarEmailAtaBool;
	}

	public AcessoCFTV getCftv() {
		return cftv;
	}

	public void setCftv(AcessoCFTV cftv) {
		this.cftv = cftv;
	}

	public ArquivoTutorialCFTV getTutorialCftv() {
		return tutorialCftv;
	}

	public void setTutorialCftv(ArquivoTutorialCFTV tutorialCftv) {
		this.tutorialCftv = tutorialCftv;
	}

	public ArquivoBoletoExterno getArquivoBoleto() {
		return arquivoBoleto;
	}

	public void setArquivoBoleto(ArquivoBoletoExterno arquivoBoleto) {
		this.arquivoBoleto = arquivoBoleto;
	}

	public BoletoExterno getBoletoExterno() {
		return boletoExterno;
	}

	public void setBoletoExterno(BoletoExterno boletoExterno) {
		this.boletoExterno = boletoExterno;
	}

	public ParametroSistema getParametroMinimoDias() {
		return parametroMinimoDias;
	}

	public void setParametroMinimoDias(ParametroSistema parametroMinimoDias) {
		this.parametroMinimoDias = parametroMinimoDias;
	}
	
}

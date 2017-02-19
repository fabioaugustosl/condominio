package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.boleto.util.ValidadorUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.AcessoCFTV;
import br.com.virtz.condominio.entidades.ArquivoDocumento;
import br.com.virtz.condominio.entidades.ArquivoTutorialCFTV;
import br.com.virtz.condominio.entidades.BoletoExterno;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class BoletoExternoController {
	
	@EJB
	private ICondominioService condominioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@Inject
	private MessageHelper messageHelper;
	
	
	private BoletoExterno boleto = null;
	
	
	@PostConstruct
	public void init(){
		boleto = condominioService.recuperarBoletoExterno(sessao.getUsuarioLogado().getCondominio().getId());
	}
	
	
	public boolean possuiBoletoExterno(){
		if(boleto != null && boleto.getUrl() != null){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public String getImagem(){
		if(boleto.getLogomarca() != null){
			return "/arquivos/"+boleto.getLogomarca().getNome();
		}
		return null;
	}
	
	
	/**
	 * Indica se a url cadastrado é um email.
	 * Caso nao seja deve ser considerada uma url para site.
	 * @return
	 */
	public boolean linkEhEmail(){
		ValidadorUtil v = new ValidadorUtil();
		if(boleto != null && boleto.getUrl() != null && v.validateEmail(boleto.getUrl())){
			return true;
		}
		return false;
	}
	
	
	public void enviarEmailSolicitacao(){
		Usuario logado = sessao.getUsuarioLogado();
				
		Map<Object, Object> map = new HashMap<Object, Object>();

		map.put("nome_usuario", logado.getNomeExibicao());
		map.put("nome_condominio", logado.getCondominio().getNome());
		map.put("nome_completo", logado.getNome());
		map.put("email", logado.getEmail());
		map.put("fone", logado.getCelular());
		map.put("nome_unidade", logado.getCondominio().getNomeUnidade());
		map.put("nome_apto", logado.getApartamento().getNumero());
		map.put("nome_bloco", logado.getApartamento().getBloco().getNome());
		map.put("nome_agrupamento", (logado.getApartamento().getBloco().getAgrupamentoUnidades() != null) ? logado.getApartamento().getBloco().getAgrupamentoUnidades().getNome() : " " );
		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		String msgEnviar = leitor.processarTemplate(caminho, EnumTemplates.SOLICITACAO_SEGUNDA_VIA_BOLETO.getNomeArquivo(), map);
		
		Email email = new Email(EnumTemplates.SOLICITACAO_SEGUNDA_VIA_BOLETO.getDe(), boleto.getUrl(), EnumTemplates.SOLICITACAO_SEGUNDA_VIA_BOLETO.getAssunto(), msgEnviar);
		enviarEmail.enviar(email);
		
		messageHelper.addInfo("Foi enviado um email para o responsável ("+boleto.getUrl()+") solicitando sua segunda via de boleto! ");
	}
	
	
	// GETTERS E SETTERS

	public BoletoExterno getBoleto() {
		return boleto;
	}

	public void setBoleto(BoletoExterno boleto) {
		this.boleto = boleto;
	}

}

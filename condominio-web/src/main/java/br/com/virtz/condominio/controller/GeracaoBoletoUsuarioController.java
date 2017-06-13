package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.service.IPublicidadeService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class GeracaoBoletoUsuarioController extends BoletoController {

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private IFinanceiroService financeiroService;	
	
	@EJB
	private ICondominioService condominioService;
	
	@EJB
	private IPublicidadeService publicidadeService;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@Inject
	private ArquivosUtil arquivosUtil;
	
	@Inject
	private LeitorTemplate leitor;
	
	
	private List<CobrancaUsuario> cobrancas = null;
	private Usuario usuario = null;
	
	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		cobrancas  = financeiroService.recuperarCobrancasUsuario(usuario.getCondominio().getId(), usuario.getId());
		if(cobrancas == null || cobrancas.isEmpty()){
			message.addWarn("Ainda não foram geradas cobranças para você!");
		}
		
		leitor.setPublicidadeService(publicidadeService);
	}
	
	
	
	public StreamedContent download(CobrancaUsuario cobranca) {   
			
		File arquivoBoleto = this.gerar(cobranca, usuario);
			
		if(arquivoBoleto != null){
			message.addInfo("Download!");
			InputStream stream;
			try {
				stream = new FileInputStream(arquivoBoleto);
				StreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "boleto_"+cobranca.getAno()+cobranca.getMes()+".pdf");
				message.addInfo("Boleto gerado!");
				return file;
			} catch (FileNotFoundException e) {
				message.addError("Aconteceu um erro inesperado ao gerar o boleto.  "+ e.getMessage());
			}
		 }
		 message.addError("Ops. Alguma coisa aconteceu de errado. Seu boleto não pode ser gerado.");
		 return null;
		
    }

	
	public void enviar(CobrancaUsuario cobranca) {   
		if(StringUtils.isBlank(usuario.getEmail())){
			message.addError("Email não cadastrado.");
			return;
		}
		
		File arquivoBoleto = this.gerar(cobranca, usuario);
		if(arquivoBoleto != null){
			
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("nome_condominio", usuario.getCondominio().getNome());
			map.put("nome_usuario", usuario.getNome());
			map.put("ano_mes", cobranca.getAnoMes());
			
			String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail();
			String msgEnviar = leitor.processarTemplate(usuario.getCondominio().getId(),caminho, EnumTemplates.BOLETO.getNomeArquivo(), map);
			
			Email email = new Email(EnumTemplates.BOLETO.getDe(), usuario.getEmail(), EnumTemplates.BOLETO.getAssunto(), msgEnviar);
			email.setAnexo(arquivosUtil.converter(arquivoBoleto));
			email.setNomeAnexo("Boleto_condominio_"+cobranca.getAnoMes()+".pdf");
			enviarEmail.enviar(email);
			
			message.addInfo("Boleto enviado com sucesso. Aguarde alguns minutos e verifique seu email.");
			
		 } else {
			 message.addError("Ops. Alguma coisa aconteceu de errado. Seu boleto não pode ser gerado.");
		 }
				
    }

	
	
	public List<CobrancaUsuario> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<CobrancaUsuario> cobrancas) {
		this.cobrancas = cobrancas;
	}

		
}

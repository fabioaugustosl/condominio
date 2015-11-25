package br.com.virtz.condominio.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;

import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IFinanceiroService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class GeracaoBoletoController {

	@EJB
	private IFinanceiroService financeiroService;	
	
	@Inject
	private DownloadBoletoController downloadController;
	
	@Inject
	private SessaoUsuario sessao;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@Inject
	private ArquivosUtil arquivosUtil;
	
	@Inject
	private LeitorTemplate leitor;
	
	@Inject
	private MessageHelper message;
	
	
	
	private List<CobrancaUsuario> cobrancas = null;
	private List<CobrancaUsuario> cobrancasSelecionadas = null;
	private Usuario usuario = null;
	
	private List<String> anosMeses = null;
	private String anoMes= null;

	
		
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		anosMeses  = financeiroService.recuperarAnosMesesDispiniveis(usuario.getCondominio().getId());
		if(anosMeses != null && !anosMeses.isEmpty()){
			anoMes = anosMeses.get(0);
			listarUsuarios();
		}
		cobrancasSelecionadas= new ArrayList<CobrancaUsuario>();
	}
	

	public void listarUsuarios(){
		cobrancas = new ArrayList<CobrancaUsuario>();
		if(StringUtils.isNotBlank(anoMes)){
			String[] anoMesSelecionado = anoMes.split("/");
			cobrancas =  financeiroService.recuperarCobrancas(usuario.getCondominio().getId(), Integer.parseInt(anoMesSelecionado[0]), Integer.parseInt(anoMesSelecionado[1]));
		}
	}
	
	
	public StreamedContent download() {
		if(cobrancasSelecionadas == null || cobrancasSelecionadas.isEmpty()){
			return null;
		}
		return downloadController.download(cobrancasSelecionadas);
    }
	
	
	public void enviar() {   
		if(cobrancasSelecionadas == null || cobrancasSelecionadas.isEmpty()){
			return;
		}
		
		List<String> usuarioSemEmail = new ArrayList<String>();
		int totalEnviados = 0;
		for(CobrancaUsuario cobranca : cobrancasSelecionadas){
			
			if(StringUtils.isBlank(cobranca.getUsuario().getEmail())){
				usuarioSemEmail.add(cobranca.getUsuario().getNome());
				continue;
			}

			File arquivoBoleto = downloadController.gerar(cobranca, cobranca.getUsuario());
			
			if(arquivoBoleto != null){
				enviarEmail(cobranca, arquivoBoleto);
				totalEnviados++;
			} 
		}
		
		if(totalEnviados > 0){
			message.addInfo("Boleto(s) enviado(s) com sucesso. ");
		}
		
		montarMensagemUsuarioNaoEnviados(usuarioSemEmail);
		cobrancasSelecionadas= new ArrayList<CobrancaUsuario>();
    }


	private void montarMensagemUsuarioNaoEnviados(List<String> usuarioSemEmail) {
		if(!usuarioSemEmail.isEmpty()){
			StringBuilder sb = new StringBuilder(" Alguns usuário não possuem emails cadastrados. Por isso não foi possivel enviar o boleto. São eles: ");
			Iterator<String> it = usuarioSemEmail.iterator();
			while(it.hasNext()){
				String n = it.next();
				sb.append(n);
				if(it.hasNext()){
					sb.append(", ");
				}
			}
			sb.append(".");
			message.addWarn(sb.toString());
		}
	}


	private void enviarEmail(CobrancaUsuario cobranca, File arquivoBoleto) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("nome_condominio", usuario.getCondominio().getNome());
		map.put("nome_usuario", cobranca.getUsuario().getNome());
		map.put("ano_mes", cobranca.getAnoMes());
		
		String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail();
		String msgEnviar = leitor.processarTemplate(caminho, EnumTemplates.BOLETO.getNomeArquivo(), map);
		
		Email email = new Email(EnumTemplates.BOLETO.getDe(), cobranca.getUsuario().getEmail(), EnumTemplates.BOLETO.getAssunto(), msgEnviar);
		email.setAnexo(arquivosUtil.converter(arquivoBoleto));
		email.setNomeAnexo("Boleto_condominio_"+cobranca.getAnoMes()+".pdf");
		enviarEmail.enviar(email);
	}


	
	
	public List<CobrancaUsuario> getCobrancas() {
		return cobrancas;
	}

	public void setCobrancas(List<CobrancaUsuario> cobrancas) {
		this.cobrancas = cobrancas;
	}

	public List<String> getAnosMeses() {
		return anosMeses;
	}

	public void setAnosMeses(List<String> anosMeses) {
		this.anosMeses = anosMeses;
	}

	public String getAnoMes() {
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}

	public List<CobrancaUsuario> getCobrancasSelecionadas() {
		return cobrancasSelecionadas;
	}

	public void setCobrancasSelecionadas(List<CobrancaUsuario> cobrancasSelecionadas) {
		this.cobrancasSelecionadas = cobrancasSelecionadas;
	}
		
		
}

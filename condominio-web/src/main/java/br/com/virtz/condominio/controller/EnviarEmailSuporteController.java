package br.com.virtz.condominio.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.util.IArquivosUtil;

@ManagedBean
@ViewScoped
public class EnviarEmailSuporteController {

	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
		
	
	@PostConstruct
	public void init(){
	}
	
	
	public void enviarEmail(String msg, String excecao, Long idCondominio) {
		StringBuffer sb = null;
		
		DataUtil dataUtil = new DataUtil();
		
		sb.append("Aconteceu um erro no sistema. ").append(dataUtil.formatarData(dataUtil.agora().getTime(),"dd/MM/yyyy")).append("<br />"); 
		sb.append("Condominio:  ").append(idCondominio).append("<br />");
		sb.append("Descrição: <br /> ").append(msg);
		sb.append("<br /><br />Exceção: <br /> ").append(excecao);

		
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		
		mapParametrosEmail.put("titulo", "Erro no sistema!");
		mapParametrosEmail.put("msg", sb.toString());


		String msgEnviar = leitor.processarTemplate(idCondominio, caminho, EnumTemplates.PADRAO.getNomeArquivo(), mapParametrosEmail);
		
		Email email = new Email(EnumTemplates.PADRAO.getDe(), EnumTemplates.PADRAO.getDe(), EnumTemplates.PADRAO.getAssunto(), msgEnviar);
		enviarEmail.enviar(email);
	}
	
	
		
}

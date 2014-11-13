package br.com.virtz.condominio.email.template;

import java.io.File;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import br.com.virtz.email.template.GerarTemplate;
import br.com.virtz.email.template.IGerarTemplate;

@ApplicationScoped
public class LeitorTemplate {
	
	private IGerarTemplate gerar = null;
	
	public LeitorTemplate() {
		super();
	}

	public String processarTemplate(String caminhoTemplate, String template, Map<Object, Object> parametros){
		gerar = new GerarTemplate();
		
		File arquivo = new File(caminhoTemplate);
		
		try {
			return gerar.gerar(parametros, arquivo, template);
		} catch (Exception e) {
			return null;
		}
	}
}

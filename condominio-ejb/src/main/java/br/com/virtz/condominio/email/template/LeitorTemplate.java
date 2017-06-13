package br.com.virtz.condominio.email.template;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import br.com.virtz.condominio.constantes.EnumTipoPublicidade;
import br.com.virtz.condominio.entidades.Publicidade;
import br.com.virtz.condominio.service.IPublicidadeService;
import br.com.virtz.email.template.GerarTemplate;
import br.com.virtz.email.template.IGerarTemplate;

@ApplicationScoped
public class LeitorTemplate {
	
	private IPublicidadeService publicidadeService;
	
	private Map<Long, Publicidade> cachePublicidade = null;
	private IGerarTemplate gerar = null;
	
	public LeitorTemplate() {
		super();
		cachePublicidade = new HashMap<Long, Publicidade>();
		
	}

	public String processarTemplate(Long idCondominio, String caminhoTemplate, String template, Map<Object, Object> parametros){
		gerar = new GerarTemplate();
		
		File arquivo = new File(caminhoTemplate);
		
		if(idCondominio != null && publicidadeService != null){
			
			Publicidade publicidade = cachePublicidade.get(idCondominio);
			if(publicidade == null){
				publicidade = publicidadeService.recuperar(idCondominio, EnumTipoPublicidade.EMAIL);
			}
			if(publicidade != null){
				parametros.put("parceiro", publicidade.getLinkImagem());
				cachePublicidade.put(idCondominio, publicidade);
			}
		}
		
		try {
			return gerar.gerar(parametros, arquivo, template);
		} catch (Exception e) {
			return null;
		}
	}

	
	public void setPublicidadeService(IPublicidadeService publicidadeService) {
		this.publicidadeService = publicidadeService;
	}
	
	
}

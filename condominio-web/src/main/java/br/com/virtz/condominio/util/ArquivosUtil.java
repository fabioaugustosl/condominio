package br.com.virtz.condominio.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ArquivosUtil {
	private static final String DIRETORIO_PADRAO_TEMPLATES = "WEB-INF\\templates\\email";

	public String getCaminhaPastaTemplatesEmail(){
		String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	    if(path.contains("/")){
	    	path +=DIRETORIO_PADRAO_TEMPLATES.replace("\\", "/");
	    } else {
	    	path +=DIRETORIO_PADRAO_TEMPLATES;
	    }
	    
	    return path;
	}
	
}

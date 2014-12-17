package br.com.virtz.condominio.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.service.IParametroSistemaService;

@SessionScoped
public class ParametroSistemaLookup implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Long, ParametroSistema> mapParametros = new HashMap<Long, ParametroSistema>();
	
	@EJB
	private IParametroSistemaService parametroService;
	
	public void iniciarLookup(Condominio condominio){
		mapParametros.clear();
		List<ParametroSistema> parametros = parametroService.recuperarTodos(condominio);
		if(parametros != null){
			for(ParametroSistema p : parametros){
				mapParametros.put(p.getId(), p);
			}
		}
	}
	
	public ParametroSistema buscar(EnumParametroSistema parametro){
		return mapParametros.get(parametro.getId());
	}
}

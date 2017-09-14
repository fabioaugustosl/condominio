package br.com.csc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.service.ICondominioService;


@Path("/condominio")
public class CondominioResource {

	@EJB
	private ICondominioService condominioService;



    @GET
    @Path("/areas/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AreaComum> getAreasPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	Condominio c = condominioService.recuperarPorId(idCondominio);
    	List<AreaComum> areas = new ArrayList<AreaComum>();
    	if(c !=null && c.getAreasComuns() !=null){
    		for(AreaComum a : c.getAreasComuns()){
    			a.setCondominio(null);
    			areas.add(a);
    		}
    	}
    	return areas;
    }


    @GET
    @Path("/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Condominio getCondominio(@PathParam("idCondominio") Long idCondominio) {
    	Condominio c = condominioService.recuperarPorId(idCondominio);
    	c.setUsuarios(null);
    	c.setBlocos(null);
    	c.getCidade().setCondominios(null);
    	c.setCftv(null);
    	if(c.getAreasComuns() != null){
    		for(AreaComum a : c.getAreasComuns()){
    			a.setCondominio(null);
    		}
    	}
    	return c;
    }



}

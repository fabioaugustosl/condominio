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
import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.service.INoticiaService;


@Path("/noticia")
public class NoticiaResource {

	@EJB
	private INoticiaService noticiaService;



    @GET
    @Path("/condominio/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Noticia> getNoticiaPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<Noticia> noticias = noticiaService.recuperarNoticiasAtivas(idCondominio, 6);
    	if(noticias !=null){
    		for(Noticia a : noticias){
    			a.setCondominio(null);
    			if(a.getArquivos()!=null){
    				for(ArquivoNoticia arq : a.getArquivos()){
    					arq.setNoticia(null);
    				}
    			}
    		}
    	}
    	return noticias;
    }


    @GET
    @Path("/{idNoticia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Noticia getNoticia(@PathParam("idNoticia") Long idNoticia) {
    	Noticia n = noticiaService.recuperarNoticia(idNoticia);
    	return n;
    }



}

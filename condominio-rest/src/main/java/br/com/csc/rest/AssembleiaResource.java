package br.com.csc.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.UsuarioService;


@Path("/assembleia")
public class AssembleiaResource {

	@EJB
	private IAssembleiaService assembleiaService;



    @GET
    @Path("/naorealizadas/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Assembleia> getAssembleiaNaoRealizadasPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<Assembleia> assembleias = assembleiaService.recuperarAssembleiasNaoRealizadas(idCondominio);
    	if(assembleias  !=null){
    		for(Assembleia a : assembleias){
    			ajustarAssembleiaParaRetorno(a);
    		}
    	}
    	return assembleias;
    }

    @GET
    @Path("/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Assembleia> getAssembleiasCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<Assembleia> assembleias = assembleiaService.recuperarTodos(idCondominio);
    	if(assembleias  !=null){
    		for(Assembleia a : assembleias){
    			ajustarAssembleiaParaRetorno(a);
    		}
    	}
    	return assembleias;
    }


    @GET
    @Path("/ultima/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public Assembleia getUltimaAssembleiaPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	Assembleia a = assembleiaService.recuperarUltimaAssembleiasRealizadas(idCondominio);
    	ajustarAssembleiaParaRetorno(a);
    	return a;
    }



	protected void ajustarAssembleiaParaRetorno(Assembleia a) {
		a.setPautas(null);
		/*if(a.getPautasAprovadas() != null){
			for(PautaAssembleia p : a.getPautasAprovadas()){
				p.setAssembleia(null);
				p.setUsuario(null);
			}
		}*/
		a.setCondominio(null);
	}



}

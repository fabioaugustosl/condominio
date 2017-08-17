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
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.UsuarioService;


@Path("/fale")
public class FaleSindicoResource {
    
	@EJB
	private IMensagemSindicoService msgService;
	    
   
    
    @GET 
    @Path("/todos/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MensagemSindico> getMensagensPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<MensagemSindico> assembleias = msgService.recuperarTodos(idCondominio);
    	if(assembleias  !=null){
    		for(MensagemSindico a : assembleias){
    			ajustarMensagemParaRetorno(a);
    		}
    	}
    	return assembleias;
    }
    
    
    @POST 
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response novaReserva(Reserva reserva ) {
    	return Response.status(500).entity("---").build();
    }
    

	protected void ajustarMensagemParaRetorno(MensagemSindico a) {
		a.setCondominio(null);
		if(a.getRespostas() != null){
			for(RespostaMensagemSindico r : a.getRespostas()){
				r.setMensagemSindico(null);
				r.getUsuario().setCondominio(null);
				r.getUsuario().getApartamento().setUsuario(null);
				r.getUsuario().getApartamento().getBloco().setCondominio(null);
				r.getUsuario().getApartamento().getBloco().setApartamentos(null);
				r.getUsuario().getApartamento().getBloco().setAgrupamentoUnidades(null);
			}
		}
	}
    

    
    
}

package br.com.csc.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.service.IVisitanteService;

@Path("/visitante")
public class VisitanteResource {


	@EJB
	private IVisitanteService visitanteService;



	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(Visitante visitante) {

		if(visitante == null){
    		return Response.status(500).entity("Visitante não enviado").build();
    	}
    	if(visitante.getCondominio() == null){
    		return Response.status(500).entity("Condomínio não informado").type(MediaType.TEXT_PLAIN).build();
    	}
    	if(visitante.getDataEntrada() == null){
    		return Response.status(500).entity("Data da entrada não prenchida").type(MediaType.TEXT_PLAIN).build();
    	}

    	/*Calendar c = Calendar.getInstance();
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.MINUTE, 0);
    	c.set(Calendar.MINUTE, 0);

    	if(c.getTime().before(new Date())){
    		return Response.status(500).entity("Não é possível criar uma notificação retroativa.").type(MediaType.TEXT_PLAIN).build();
    	}*/

    	try {
			visitanteService.salvarVisitante(visitante);
			return Response.status(200).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(500).entity("Ocorreu um erro ao registrar visitante.").type(MediaType.TEXT_PLAIN).build();
    }


	@GET
    @Path("/condominio/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Visitante> getNotificacaoPortariaPorCondominio(@PathParam("idCondominio") Long idCondominio, @QueryParam("de") Integer de, @QueryParam("para") Integer para) {

		if(de == null){
			de = 0;
		}

		if(para == null){
			para = 30;
		}

    	List<Visitante> noticias = visitanteService.recuperarPorCondominioPaginado(idCondominio, de, para);
    	if(noticias !=null){
    		for(Visitante a : noticias){
    			prepararObjetoParaRetorno(a);
    		}
    	}
    	return noticias;
    }


	protected void prepararObjetoParaRetorno(Visitante a) {
		a.setCondominio(null);

		if(a.getApartamento() !=null){
			a.getApartamento().setUsuario(null);
			a.getApartamento().getBloco().setApartamentos(null);
			a.getApartamento().getBloco().setAgrupamentoUnidades(null);
			a.getApartamento().getBloco().setCondominio(null);
		}

	}


}

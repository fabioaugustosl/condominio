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

import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.UsuarioService;


@Path("/reserva")
public class ReservaResource {

	@EJB
	private IReservaService reservaService;

	@EJB
	private IUsuarioService usuarioService;



    @GET
    @Path("/condominio/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reserva> getReservasPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<Reserva> reservas = reservaService.recuperarPorCondominio(idCondominio);
    	if(reservas  !=null){
    		for(Reserva a : reservas){
    			ajustarReservaParaRetorno(a);
    		}
    	}
    	return reservas;
    }


    @DELETE
    @Path("/{idReserva}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removerReserva(@PathParam("idReserva") Long idReserva) {
    	reservaService.remover(idReserva);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response novaReserva(Reserva reserva ) {

    	if(reserva == null){
    		return Response.status(500).entity("Reserva não enviada").build();
    	}
    	if(reserva.getData() == null){
    		return Response.status(500).entity("Data da reserva não preenchida").build();
    	}
    	if(reserva.getAreaComum() == null){
    		return Response.status(500).entity("Área da reserva não informada").build();
    	}
    	if(reserva.getUsuario() == null){
    		return Response.status(500).entity("Morador responsável pela reserva não informado").build();
    	}

    	Usuario u = usuarioService.recuperarUsuarioCompleto(reserva.getUsuario().getId());
    	reserva.setApartamento(u.getApartamento());
    	reserva.setUsuario(u);

    	try {
			Reserva r =  reservaService.salvar(reserva);
			return Response.status(200).entity(r).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(500).entity("Ocorreu um erro ao efetuar a reserva").build();
    }



	protected void ajustarReservaParaRetorno(Reserva a) {
		if(a.getApartamento() != null && a.getApartamento().getBloco() != null){
			a.getApartamento().getBloco().setApartamentos(null);
			a.getApartamento().getBloco().setCondominio(null);
		}
		if(a.getAreaComum() !=null){
			a.getAreaComum().setCondominio(null);
		}
		if(a.getUsuario() !=null){
			a.getUsuario().setCondominio(null);
			a.getUsuario().setApartamento(null);
			a.getUsuario().setUnidade(null);
		}
	}



}

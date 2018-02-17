package br.com.csc.rest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.virtz.condominio.entidades.ArquivoNoticia;
import br.com.virtz.condominio.entidades.ArquivoNotificacaoPortaria;
import br.com.virtz.condominio.entidades.Noticia;
import br.com.virtz.condominio.entidades.NotificacaoPortaria;
import br.com.virtz.condominio.service.INotificacaoPortariaService;

@Path("/portaria")
public class NotificarPortariaResource {


	@EJB
	private INotificacaoPortariaService notificacaoService;



	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response notificarPortaria(NotificacaoPortaria notificacao) {

		if(notificacao == null){
    		return Response.status(500).entity("Notificação não enviada").build();
    	}
    	if(notificacao.getDataPrevista() == null){
    		return Response.status(500).entity("Data da reserva não preenchida").type(MediaType.TEXT_PLAIN).build();
    	}
    	if(notificacao.getUsuario() == null){
    		return Response.status(500).entity("Morador responsável pela reserva não informado").type(MediaType.TEXT_PLAIN).build();
    	}

    	Calendar c = Calendar.getInstance();
    	c.setTime(notificacao.getDataPrevista());
    	c.add(Calendar.DAY_OF_YEAR, 1);
    	notificacao.setDataPrevista(c.getTime());

    	if(c.getTime().before(new Date())){
    		return Response.status(500).entity("Não é possível criar uma notificação retroativa.").type(MediaType.TEXT_PLAIN).build();
    	}

    	try {
			notificacaoService.salvar(notificacao);
			return Response.status(200).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(500).entity("Ocorreu um erro ao registrar notificação.").type(MediaType.TEXT_PLAIN).build();
    }


	@GET
    @Path("/condominio/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NotificacaoPortaria> getNotificacaoPortariaPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<NotificacaoPortaria> noticias = notificacaoService.recuperarPorCondominio(idCondominio);
    	if(noticias !=null){
    		for(NotificacaoPortaria a : noticias){
    			prepararObjetoParaRetorno(a);
    		}
    	}
    	return noticias;
    }


	@GET
    @Path("/ultimas/condominio/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NotificacaoPortaria> getUltimasNotificacaoPortariaPorCondominio(@PathParam("idCondominio") Long idCondominio) {
    	List<NotificacaoPortaria> noticias = notificacaoService.recuperarUltimasNotificacoes(idCondominio);
    	if(noticias !=null){
    		for(NotificacaoPortaria a : noticias){
    			prepararObjetoParaRetorno(a);
    		}
    	}
    	return noticias;
    }



	protected void prepararObjetoParaRetorno(NotificacaoPortaria a) {
		a.setCondominio(null);

		if(a.getUsuario() !=null){
			a.getUsuario().setCondominio(null);
			a.getUsuario().setApartamento(null);
			a.getUsuario().setUnidade(null);
		}
	}



}

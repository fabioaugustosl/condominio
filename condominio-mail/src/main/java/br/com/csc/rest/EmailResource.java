package br.com.csc.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.csc.util.EnviadorEmail;


@Path("/v1/email")
public class EmailResource {


    @POST
    public Response enviarEmail(@FormParam("de") String de,
    		@FormParam("para") String para,
    		@FormParam("assunto") String assunto,
    		@FormParam("mensagem") String mensagem,
    		@FormParam("responderPara") String responderPara) {

    	EnviadorEmail enviarEmail = new EnviadorEmail();

    	if(mensagem == null){
    		return Response.status(500).entity("Mensagem não enviada").build();
    	}
    	if(para == null){
    		return Response.status(500).entity("Mensagem não preenchida").type(MediaType.TEXT_PLAIN).build();
    	}


    	enviarEmail.enviar(de, para, assunto, mensagem, responderPara);

		return Response.status(200).entity(mensagem).build();

		//return Response.status(500).entity("Ocorreu um erro ao registrar mensagem enviada ao síndico.").type(MediaType.TEXT_PLAIN).build();
    }




}

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
import br.com.virtz.condominio.entidades.Notificacao;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.service.INoticiaService;
import br.com.virtz.condominio.service.INotificacaoService;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.UsuarioService;


@Path("/notificacao")
public class NotificacaoMoradorResource {
    
	@EJB
	private INotificacaoService nService;
	
	@EJB
	private IUsuarioService usuarioService;
	    
   
    
    @GET 
    @Path("/morador/{idMorador}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notificacao> getMensagensPorCondominio(@PathParam("idMorador") Long idMorador) {
    	Usuario u = usuarioService.recuperarUsuarioCompleto(idMorador);
    	List<Notificacao> notificacoes = nService.recuperar(u.getCondominio().getId().longValue(), u.getId());
    	if(notificacoes != null){
	    	for(Notificacao r : notificacoes){
	    		r.setCondominio(null);
				r.getUsuario().setCondominio(null);
				r.getUsuario().getApartamento().setUsuario(null);
				r.getUsuario().getApartamento().getBloco().setCondominio(null);
				r.getUsuario().getApartamento().getBloco().setApartamentos(null);
				r.getUsuario().getApartamento().getBloco().setAgrupamentoUnidades(null);
			}
    	}
    	return notificacoes;
    }
    
    
    
}

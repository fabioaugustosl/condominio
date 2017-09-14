package br.com.csc.rest;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.csc.util.IArquivosUtil;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.service.IAssembleiaService;


@Path("/assembleia")
public class AssembleiaResource {

	@EJB
	private IAssembleiaService assembleiaService;

	@Inject
	private IArquivosUtil arquivoUtil;



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
		//a.setPautas(null);
		if(a.getPautas() != null){
			for(PautaAssembleia p : a.getPautas()){
				p.setAssembleia(null);
				p.setUsuario(null);
			}
		}
		a.setCondominio(null);
	}


	@GET
    @Path("/ata/{idAssembleia}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("idAssembleia") Long idAssembleia) {
		Assembleia a = assembleiaService.recuperar(idAssembleia);
		if(a == null){
			return Response.status(500).entity("Não foi possível recuperar essa assembleia.").type(MediaType.TEXT_PLAIN).build();
		}

		if(a.getArquivoAta() == null){
			return Response.status(500).entity("Não foi possível recuperar a ata da assembleia.").type(MediaType.TEXT_PLAIN).build();
		}

		File f = new File(arquivoUtil.getCaminhoArquivosUpload()+a.getArquivoAta().getNome());
		ResponseBuilder response = Response.ok((Object) f);
        response.header("Content-Disposition", "attachment; filename=ata_"+a.getData()+".pdf");
        return response.build();

    }


}

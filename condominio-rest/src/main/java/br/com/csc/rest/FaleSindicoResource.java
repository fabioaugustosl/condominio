package br.com.csc.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.csc.util.ArquivosUtil;
import br.com.csc.util.EnumTemplatesRestRefatorar;
import br.com.csc.util.EnviadorEmail;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.EmailParaEnviar;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.service.IEmailEnviarService;
import br.com.virtz.condominio.service.IMensagemSindicoService;
import br.com.virtz.condominio.service.IUsuarioService;


@Path("/fale")
public class FaleSindicoResource {

	@EJB
	private IMensagemSindicoService msgService;

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private IEmailEnviarService emailService;

	@Inject
	private ArquivosUtil arquivosUtil;

	@Inject
	private LeitorTemplate leitor;



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
    public Response novaMensagem(MensagemSindico mensagem,  @Context ServletContext context) {
    	if(mensagem == null){
    		return Response.status(500).entity("Mensagem não enviada").build();
    	}
    	if(mensagem.getMensagem() == null){
    		return Response.status(500).entity("Mensagem não preenchida").type(MediaType.TEXT_PLAIN).build();
    	}
    	if(mensagem.getUsuario() == null){
    		return Response.status(500).entity("Morador não informado").type(MediaType.TEXT_PLAIN).build();
    	}
    	mensagem.setData(new Date());

    	Usuario u = usuarioService.recuperarUsuarioCompleto(mensagem.getUsuario().getId());
    	String nomeUsuario = u.getNomeExibicao();
    	mensagem.setCondominio(u.getCondominio());

    	try {
    		mensagem = msgService.salvar(mensagem);

    		try {
				List<Usuario> sindicos = usuarioService.recuperarSindicos(mensagem.getCondominio().getId());
				envioEmail(sindicos, nomeUsuario, mensagem, context);
			}catch(Exception e1){
			}

    		//5mensagem.getCondominio().setBlocos(null);

			return Response.status(200).entity(mensagem).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(500).entity("Ocorreu um erro ao registrar mensagem enviada ao síndico.").type(MediaType.TEXT_PLAIN).build();
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



	private void envioEmail(List<Usuario> sindicos, String nomeUsuario, MensagemSindico msg, ServletContext context) {
		for(Usuario sindico : sindicos){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("nome_sindico", org.apache.commons.lang.StringEscapeUtils.escapeHtml(sindico.getNome()));
			map.put("nome_usuario", org.apache.commons.lang.StringEscapeUtils.escapeHtml(nomeUsuario));
			map.put("msg", org.apache.commons.lang.StringEscapeUtils.escapeHtml(msg.getMensagem()));

			String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail(context);
			String msgEnviar = leitor.processarTemplate(sindico.getCondominio().getId(),caminho, EnumTemplatesRestRefatorar.MENSAGEM_SINDICO.getNomeArquivo(), map);

			EmailParaEnviar email = new EmailParaEnviar();
			email.setDe(EnumTemplatesRestRefatorar.MENSAGEM_SINDICO.getDe());
			email.setPara(sindico.getEmail());
			email.setAssunto(EnumTemplatesRestRefatorar.MENSAGEM_SINDICO.getAssunto());
			email.setMensagem(msgEnviar);
			email.setResponderPara(sindico.getEmail());

			try {
				emailService.salvar(email);
			} catch (Exception e) {
				System.out.println("ocorreu um erro ao salvar email para enviar");
			}
		}

	}



}

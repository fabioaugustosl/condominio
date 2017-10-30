package br.com.csc.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.csc.rest.bean.ReservaRest;
import br.com.csc.util.ArquivosUtil;
import br.com.csc.util.EnumTemplatesRestRefatorar;
import br.com.csc.util.EnviadorEmail;
import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.EmailParaEnviar;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IEmailEnviarService;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;


@Path("/reserva")
public class ReservaResource {

	@EJB
	private IReservaService reservaService;

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private IEmailEnviarService emailService;

	private EnviadorEmail enviarEmail;

	@Inject
	private ArquivosUtil arquivosUtil;

	@Inject
	private LeitorTemplate leitor;

	@Inject
	private ParametroSistemaLookup parametroLookup;


    @GET
    @Path("/condominio/{idCondominio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservaRest> getReservasPorCondominio(@PathParam("idCondominio") Integer idCondominio) {
    	List<Reserva> reservas = reservaService.recuperarReservarRecentesPorCondominio(idCondominio);
    	List<ReservaRest> reservasRet = new ArrayList<ReservaRest>();
    	if(reservas  !=null){
    		Calendar cal = Calendar.getInstance();
    		Integer anoAtual = cal.get(Calendar.YEAR);
    		Integer mesAtual = cal.get(Calendar.MONTH);
    		for(Reserva a : reservas){
    			Integer ano = a.getData().get(Calendar.YEAR);
    			Integer mes = a.getData().get(Calendar.MONTH);
    			if(ano < anoAtual || mes < mesAtual){
    				continue;
    			}
    			ReservaRest reserva = new ReservaRest(a);
    			ajustarReservaParaRetorno(reserva);
    			reservasRet.add(reserva);
    		}
    	}
    	return reservasRet;
    }


    @DELETE
    @Path("/{idReserva}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removerReserva(@PathParam("idReserva") Long idReserva) throws Exception {
    	reservaService.remover(idReserva);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response novaReserva(Reserva reserva ,  @Context ServletContext context)  {

    	if(reserva == null){
    		return Response.status(500).entity("Reserva não enviada").build();
    	}
    	if(reserva.getData() == null){
    		return Response.status(500).entity("Data da reserva não preenchida").type(MediaType.TEXT_PLAIN).build();
    	}
    	if(reserva.getAreaComum() == null){
    		return Response.status(500).entity("Área da reserva não informada").type(MediaType.TEXT_PLAIN).build();
    	}
    	if(reserva.getUsuario() == null){
    		return Response.status(500).entity("Morador responsável pela reserva não informado").type(MediaType.TEXT_PLAIN).build();
    	}

    	Usuario u = usuarioService.recuperarUsuarioCompleto(reserva.getUsuario().getId());
    	Long idCondominio = u.getCondominio().getId();
    	String nomeArea = " Aŕea ";
    	if(u.getCondominio().getAreasComuns() != null){
	    	for(AreaComum a : u.getCondominio().getAreasComuns()){
	    		if(a.getId().equals(reserva.getAreaComum().getId())){
	    			nomeArea = a.getNome();
	    		}
	    	}
    	}

    	// iniciar lookups
    	parametroLookup.iniciarLookup(u.getCondominio());

    	reserva.setApartamento(u.getApartamento());
    	reserva.setUsuario(u);

    	Calendar c = reserva.getData();
    	c.add(Calendar.DAY_OF_YEAR, 1);
    	c.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
    	reserva.setData(c);

    	// valida se já existe reserva para o dia
    	List<Reserva> reservas = reservaService.recuperarRecentes(reserva.getAreaComum());
    	if(reservas != null){
    		DataUtil dUtil = new DataUtil();
    		for(Reserva r : reservas){
    			if(dUtil.mesmoDiaMesAno(reserva.getData().getTime(), r.getData().getTime())){
    				return Response.status(500).entity("Já existe uma reserva para para este dia.").type(MediaType.TEXT_PLAIN).build();
    			}
    		}
    	}

    	try {
			reservaService.validarReserva(reserva.getData(), reserva.getAreaComum(), true);
			reservaService.verificarUsuarioBloqueado(reserva.getUsuario());
		} catch (Exception e1) {
			return Response.status(500).entity(e1.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}

    	try {
			Reserva r =  reservaService.salvar(reserva);
			ReservaRest reservaRet = new ReservaRest(r);
			ajustarReservaParaRetorno(reservaRet);
			try {
				enviarEmailConfirmacaoReserva(u, r, idCondominio, nomeArea, context);
			} catch (Exception e) { e.printStackTrace();	}
			return Response.status(200).entity(reservaRet).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Response.status(500).entity("Ocorreu um erro ao efetuar a reserva").type(MediaType.TEXT_PLAIN).build();
    }



	protected void ajustarReservaParaRetorno(ReservaRest a) {
		if(a.getApartamento() != null && a.getApartamento().getBloco() != null){
			try { a.setNomeApartamento(a.getApartamento().getNomeExibicao()); }catch(Exception e ){a.setNomeApartamento(a.getApartamento().getNumero());}
			//a.getApartamento().getBloco().setApartamentos(null);
			//a.getApartamento().getBloco().setCondominio(null);
			a.setApartamento(null);

		}
		if(a.getAreaComum() !=null){
			a.getAreaComum().setCondominio(null);
			a.getAreaComum().setInstrucoesReserva(null);
		}
		if(a.getUsuario() !=null){
			a.getUsuario().setCondominio(null);
			a.getUsuario().setApartamento(null);
			a.getUsuario().setUnidade(null);
			a.setNomeUsuario(a.getUsuario().getNomeExibicao());
		}
	}



	private void enviarEmailConfirmacaoReserva(Usuario usuario, Reserva reserva, Long idCondominio, String nomeArea, ServletContext context) {
		DataUtil dataUtil = new DataUtil();

		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("nome_usuario", org.apache.commons.lang.StringEscapeUtils.escapeHtml(usuario.getNomeExibicao()));
		mapParametrosEmail.put("data_reserva", dataUtil.formatarData(reserva.getData().getTime(),"dd/MM/yyyy"));
		mapParametrosEmail.put("nome_area", org.apache.commons.lang.StringEscapeUtils.escapeHtml(nomeArea));

		String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail(context);

		String msg = leitor.processarTemplate(idCondominio, caminho, EnumTemplatesRestRefatorar.CONFIRMACAO_RESERVA_AREA.getNomeArquivo(), mapParametrosEmail);

//		Email email = new Email(EnumTemplatesRestRefatorar.PAUTA_ENVIADA.getDe(), usuario.getEmail(), EnumTemplatesRestRefatorar.CONFIRMACAO_RESERVA_AREA.getAssunto(), msg);
//		enviarEmail.enviar(email);

		EmailParaEnviar email = new EmailParaEnviar();
		email.setDe(EnumTemplatesRestRefatorar.CONFIRMACAO_RESERVA_AREA.getDe());
		email.setPara(usuario.getEmail());
		email.setAssunto(EnumTemplatesRestRefatorar.CONFIRMACAO_RESERVA_AREA.getAssunto());
		email.setMensagem(msg);
		email.setResponderPara(usuario.getEmail());

		try {
			emailService.salvar(email);
		} catch (Exception e) {
			System.out.println("ocorreu um erro ao salvar email para enviar");
		}
	}



}

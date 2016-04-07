package br.com.virtz.condominio.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.VotacaoView;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.IVotacaoService;

@Singleton @Startup
public class TimerEnviarEmailAutomatico {
	
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@EJB
	private IVotacaoService votacaoService;
	
	@EJB
	private IAssembleiaService assembleiaService;
	
	@EJB
	private IUsuarioService usuarioService;
	

	@Schedule(dayOfWeek="Sun, Mon, Tue, Wed, Thu, Fri, Sat", hour="6", minute="00", persistent=false)
	//@Schedule(minute = "*/10",  hour="*", persistent=false)
	public void  enviarEmailVotacaoEncerrada(){
		List<Votacao> votacoes = votacaoService.recuperarVotacoesEncerradasSemEnvioDeEmail();
		if(votacoes != null && !votacoes.isEmpty()){
			for(Votacao v : votacoes){
				enviarEmailResultadoVotacao(v);
			}
		}
	}
	
	private void enviarEmailResultadoVotacao(Votacao votacao) {
		
		VotacaoView vv = null;
		// recupera os resultados
		if(votacao != null){
			vv = new VotacaoView(votacao);
			vv.getUtil().tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
			try {
				vv.setResultadoVotacaoSelecionada(votacaoService.recuperarResultado(votacao.getId()));
			} catch (AppException e) {
				e.printStackTrace();
				return;
			}
		} else{
			return;
		}
		
		List<String> resultados = new ArrayList<String>();
		try {
			// monta resultado para envio
			for(String chave : vv.getResultadoVotacaoSelecionada().keySet()){
				StringBuilder sb = new StringBuilder("[");
				sb.append(vv.getResultadoVotacaoSelecionada().get(chave)).append(" voto(s) - ").append(vv.getResultadoPercentagemVotacaoSelecionada().get(chave)).append("% ]   ");
				sb.append(chave).append("<br />");
				resultados.add(sb.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		try {
			// enviar emails
			Condominio c = votacao.getCondominio();
			List<Usuario> usuarios  = usuarioService.recuperarTodos(c);
			
			if(usuarios != null){
				for(Usuario u : usuarios){
					String msg = montarTextoEmail(u, votacao.getAssuntoVotacao(), resultados);
					Email email = new Email("contato@condominiosobcontrole.com.br", u.getEmail(), "Resultado final de votação", msg);
					enviarEmail.enviar(email);
				}
			}

			try {
				votacaoService.marcarEmailJaEnviado(votacao.getId());
			} catch (AppException e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
	}
	
	
	/**
	 * Monta o template do email a ser enviado. 
	 * Copiei e colei o resultadoVotacao.ftl.. tentar manter compatibilidade.
	 * @param u
	 * @return
	 */
	private String montarTextoEmail(Usuario u, String tituloVotacao, List<String> resultado){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		sb.append("<meta charset=\"ISO-8859-1\">");
		sb.append("</head>");

		sb.append("<body>");
		sb.append("<h1>Olá ").append(u.getNomeExibicao()).append(".</h1>");
		sb.append("<p>Terminou a votação: </p>");
		sb.append("<p> <strong>").append(tituloVotacao).append("</strong></p> <br />");
		sb.append("<p>Confira o resultado final:</p>");
		for(String r : resultado){
			sb.append("<p> ").append(r).append("</p>");
		}
		sb.append("<br /><br />");
		sb.append("<a href=\"http://www.condominiosobcontrole.com.br\">");
		sb.append("<img src=\"http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true\" title=\"Condominio SOBControle\"/><br/>www.condominiosobcontrole.com.br</a>");
			
			
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
	
	
	@Schedule(dayOfWeek="Sun, Mon, Tue, Wed, Thu, Fri, Sat", hour="10", minute="30", persistent=false)
	//@Schedule(minute = "*/10",  hour="*", persistent=false)
	public void  enviarEmailLembreteAtaAssembleia(){
		List<Assembleia> assembleias = assembleiaService.recuperarAssembleiasRealizadasSemAta();
		if(assembleias != null && !assembleias.isEmpty()){
			for(Assembleia v : assembleias){
				enviarEmailAvisandoAta(v);
			}
		}
	}
	
	
	private void enviarEmailAvisandoAta(Assembleia assembleia) {
		try {
			// enviar emails
			List<Usuario> usuarios  = usuarioService.recuperarSindicos(assembleia.getCondominio().getId());
			
			DataUtil dataUtil = new DataUtil();
			String dataAssembleia = dataUtil.formatarData(assembleia.getData(), "dd/MM/yyyy");
			if(usuarios != null){
				for(Usuario u : usuarios){
					String msg = montarTextoEmailLembreAta(u, dataAssembleia);
					Email email = new Email("contato@condominiosobcontrole.com.br", u.getEmail(), "Lembrete para disponibilização de ata", msg);
					enviarEmail.enviar(email);
				}
			}

			try {
				assembleia.setAvisoDeAta(Boolean.TRUE);
				assembleiaService.salvar(assembleia);
			} catch (AppException e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	
	
	/**
	 * Monta o template do email a ser enviado para lembrar o sindico de anexar a ata da assembleia realizada.
	 * @param u
	 * @return
	 */
	private String montarTextoEmailLembreAta(Usuario u, String data){
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
		sb.append("<meta charset=\"ISO-8859-1\">");
		sb.append("</head>");

		sb.append("<body>");
		sb.append("<h1>Olá ").append(u.getNomeExibicao()).append(".</h1>");
		sb.append("<p>Gostaríamos de lembrá-lo que ainda não foi anexada nenhuma ata referente a assembleia realizada dia ").append(data).append(".");
		sb.append("<br /><br />");
		sb.append("<br /><br />");
		sb.append("<a href=\"http://www.condominiosobcontrole.com.br\">");
		sb.append("<img src=\"http://www.condominiosobcontrole.com.br/condominio/img/logo_pequena_azul.png?pfdrid_c=true\" title=\"Condominio SOBControle\"/><br/>www.condominiosobcontrole.com.br</a>");
			
			
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
}

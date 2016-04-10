package br.com.virtz.condominio.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
	
	private ConstrutorEmailsTimer construtorEmail = null;
	
	
	@PostConstruct
	public void init(){
		construtorEmail = new ConstrutorEmailsTimer();
	}
	

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
					String msg = construtorEmail.montarTextoEmail(u, votacao.getAssuntoVotacao(), resultados);
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
	
	
	
	
	//@Schedule(dayOfWeek="Sun, Mon, Tue, Wed, Thu, Fri, Sat", hour="10", minute="30", persistent=false)
	@Schedule(minute = "*/10",  hour="*", persistent=false)
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
					String msg = construtorEmail.montarTextoEmailLembreAta(u, dataAssembleia);
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
	
	
	//@Schedule(dayOfWeek="Sun, Mon, Tue, Wed, Thu, Fri, Sat", hour="11", minute="30", persistent=false)
	@Schedule(minute = "*/5",  hour="*", persistent=false)
	public void  enviarEmailLembreteAssembleia(){
		List<Assembleia> assembleias = assembleiaService.recuperarAssembleiasQueSeraoRealizadasDaqui3dias();
		if(assembleias != null && !assembleias.isEmpty()){
			for(Assembleia v : assembleias){
				enviarEmailAvisandoLembreteAssembleiaProxima(v);
			}
		}
	}
	
	
	private void enviarEmailAvisandoLembreteAssembleiaProxima(Assembleia assembleia) {
		try {
			// enviar emails
			List<Usuario> usuarios  = usuarioService.recuperarTodos(assembleia.getCondominio());
			
			DataUtil dataUtil = new DataUtil();
			
			if(usuarios != null){
				String dataAssembleia = dataUtil.formatarData(assembleia.getData(), "dd/MM/yyyy");
				String horario1 = dataUtil.formatarData(assembleia.getHorario1(), "HH:mm");
				String horario2 = assembleia.getHorario2() != null ? dataUtil.formatarData(assembleia.getHorario2(), "HH:mm") : "-";
				for(Usuario u : usuarios){
					String msg = construtorEmail.montarTextoEmailLembreteAssembleia(u, dataAssembleia, horario1, horario2);
					Email email = new Email("contato@condominiosobcontrole.com.br", u.getEmail(), "Lembrete para assembleia", msg);
					enviarEmail.enviar(email);
				}
			}

			try {
				assembleia.setAvisoDeAssembleiaAutomatico(Boolean.TRUE);
				assembleiaService.salvar(assembleia);
			} catch (AppException e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	
	
}

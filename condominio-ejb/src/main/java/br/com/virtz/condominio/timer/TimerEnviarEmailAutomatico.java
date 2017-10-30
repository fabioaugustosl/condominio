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
import br.com.virtz.condominio.entidades.EmailParaEnviar;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.VotacaoView;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IEmailEnviarService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.IVotacaoService;

@Singleton @Startup
public class TimerEnviarEmailAutomatico {


	@EJB
	private EnviarEmail enviarEmail;

	@EJB
	private IEmailEnviarService emailService;



	@PostConstruct
	public void init(){
	}


	@Schedule(hour="*", minute="*/1", persistent=false)
	public void  enviarEmailTabela(){
		List<EmailParaEnviar> emails = emailService.recuperarNaoEnviados();
		if(emails != null && !emails.isEmpty()){
			for(EmailParaEnviar v : emails){
				enviarEmail(v);
			}
		}
	}

	private void enviarEmail(EmailParaEnviar msg) {
		// enviar emails
		try {
			Email email = new Email(msg.getDe(), msg.getPara(), msg.getAssunto(), msg.getMensagem());
			email.setResponderPara(msg.getResponderPara());
			enviarEmail.enviar(email);

			emailService.remover(msg.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}






}

package br.com.virtz.condominio.timer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton @Startup
public class TimerEnviarEmailAutomatico {

	//@Schedule(dayOfWeek="Sun, Mon, Tue, Wed, Thu, Fri, Sat", hour="11", minute="00", persistent=false)
	@Schedule(minute = "∗/5", persistent=false)
	public void  enviarEmailVotacaoEncerrada(){
		System.out.println("timer");
	}
}

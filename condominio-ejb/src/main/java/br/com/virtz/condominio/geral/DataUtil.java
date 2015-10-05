package br.com.virtz.condominio.geral;

import java.util.Calendar;
import java.util.Date;

public class DataUtil {

	/**
	 * Zera as horas, minutos e segundo de uma data.
	 * Deixando apenas a data.
	 * 
	 * @param data
	 * @return
	 */
	public Date limparHora(Date data) {
		Calendar d = Calendar.getInstance();
		d.setTime(data);
		d.set(Calendar.MILLISECOND, 0);
		d.set(Calendar.SECOND, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.HOUR, 0);
		return d.getTime();
	}
	
	
	public Calendar agora() {
		Calendar d = Calendar.getInstance();
		d.setTime(new Date());
		d.set(Calendar.MILLISECOND, 0);
		d.set(Calendar.SECOND, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.HOUR, 0);
		return d;
	}
	
	
	public Date adicionarDias(Date data, Integer quantidadeDiasParaAdicionar){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DATE, quantidadeDiasParaAdicionar);
		return c.getTime();
	}
	
	
	public boolean dataEhMaiorQueHoje(Date data) {
		Date dataHoje = limparHora(new Date());
		if(dataHoje.after(limparHora(data))){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}

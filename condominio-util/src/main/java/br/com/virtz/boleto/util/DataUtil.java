package br.com.virtz.boleto.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean dataEhMenorQueHoje(Date data) {
		Date dataHoje = limparHora(new Date());
		return !limparHora(data).after(dataHoje);
	}

	/**
	 * Lista os anos a partir no ano de inicio.
	 * Vai até 5 anos após o atual;
	 *
	 * Se o inicio for null será considerado o ano atual.
	 *
	 * @param inicio
	 * @return
	 */
	public List<Integer> listarAnosSelecao(Integer inicio){
		List<Integer> anos = new ArrayList<Integer>();

		Calendar c = agora();
		Integer anoMax = c.get(Calendar.YEAR)+5;
		if(inicio == null){
			inicio = c.get(Calendar.YEAR);
		}

		while(inicio <= anoMax){
			anos.add(inicio);
			inicio++;
		}

		return anos;
	}

	public Map<Integer, String> listarMesesSelecao(){
		Map<Integer, String> meses = new HashMap<Integer, String>();
		meses.put(1, "Janeiro");
		meses.put(2, "Fevereiro");
		meses.put(3, "Março");
		meses.put(4, "Abril");
		meses.put(5, "Maio");
		meses.put(6, "Junho");
		meses.put(7, "Julho");
		meses.put(8, "Agosto");
		meses.put(9, "Setembro");
		meses.put(10, "Outubro");
		meses.put(11, "Novembro");
		meses.put(12, "Dezembro");
		return meses;
	}

	public boolean mesmoDiaMesAno(Date data1, Date data2) {
		if(data1 == null || data2 == null){
			return false;
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(data1);
		c2.setTime(data2);

		if(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)){
			return false;
		}

		if(c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)){
			return false;
		}

		if(c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH)){
			return false;
		}

		return true;
	}


	public int diasEntreDatas(Date data1, Date data2) {
		if(data1 == null || data2 == null){
			return 0;
		}

		return Math.abs(diasEntreTimestemp(this.limparHora(data1).getTime(), this.limparHora(data2).getTime()));
	}

	private int diasEntreTimestemp(long t1, long t2) {
	    return (int) ((t2 - t1) / (1000 * 60 * 60 * 24));
	}


	public String formatarData(Date data, String formato){
		SimpleDateFormat dt1 = new SimpleDateFormat(formato);
        return dt1.format(data);
	}
}

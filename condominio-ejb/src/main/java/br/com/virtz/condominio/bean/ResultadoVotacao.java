package br.com.virtz.condominio.bean;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacao;

public class ResultadoVotacao {
	
	private Map<String, Integer> resultado;
	private EnumTipoVotacao tipoVotacao;

	public ResultadoVotacao(EnumTipoVotacao tipoVotacao) {
		super();
		resultado = new HashMap<String, Integer>();
		
		this.tipoVotacao = tipoVotacao;
		if(EnumTipoVotacao.SIM_NAO.equals(tipoVotacao)){
			resultado.put("Sim", 0);
			resultado.put("Não", 0);
		}
	}
	
	
	public void contabilizarVoto(Object opcao){
		String chave = getChaveMap(opcao);
		Integer qtdVotos = resultado.get(chave);
		if(qtdVotos == null){
			qtdVotos = 0;
		}
		resultado.put(chave, (qtdVotos+1));
	}
	
	
	private String getChaveMap(Object opcao) {
		if(EnumTipoVotacao.SIM_NAO.equals(tipoVotacao)){
			if((Boolean)opcao){
				return "Sim";
			} else {
				return "Não";
			}
		} else if(EnumTipoVotacao.OPCOES.equals(tipoVotacao)){
			OpcaoVotacao opc = (OpcaoVotacao) opcao;
			return opc.getDescricao();
		} else if(EnumTipoVotacao.DATA.equals(tipoVotacao)){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date opc = (Date) opcao;
			return sdf.format(opc);
		} else if(EnumTipoVotacao.NUMERICA.equals(tipoVotacao)){
			NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale ("pt", "BR")));  
			Double valor = (Double) opcao;  
			return nf.format(valor);  
		} else if(EnumTipoVotacao.MOEDA.equals(tipoVotacao)){
			NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale ("pt", "BR")));  
			Double valor = (Double) opcao;  
			return nf.format("R$ "+valor);  
		}
		return null;
	}


	public Map<String, Integer> resultado(){
		return resultado;
	}
	
	
}

package br.com.virtz.boleto.formatador;

import br.com.virtz.boleto.bean.EnumBanco;

public class FabricaFormatadorDadosBancarios {

	
	public static FormatadorDadosBancarios recuperarFormatador(EnumBanco banco){
		if(EnumBanco.SANTANDER.equals(banco)){
			return new FormatadorDadosBancariosSantander();
		}
		return new FormatadorDadosBancariosPadrao();
	}
	
}

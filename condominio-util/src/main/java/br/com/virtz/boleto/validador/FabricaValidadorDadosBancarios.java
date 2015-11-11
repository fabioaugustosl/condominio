package br.com.virtz.boleto.validador;

import br.com.virtz.boleto.bean.EnumBanco;

public class FabricaValidadorDadosBancarios {

	
	public static ValidadorDadosBancarios recuperarFormatador(EnumBanco banco){
		if(EnumBanco.SANTANDER.equals(banco)){
			return new ValidadorDadosBancariosSantander();
		}
		return new ValidadorDadosBancariosPadrao();
	}
	
}

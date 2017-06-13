package br.com.virtz.boleto.formatador;

import org.apache.commons.lang.StringUtils;

public class FormatadorDadosBancariosSantander extends FormatadorDadosBancarios {

	@Override
	public String formatarConta(String numeroConta) {
		if(StringUtils.isNotBlank(numeroConta)){
			if(numeroConta.length() < 6){
				return StringUtils.leftPad(numeroConta, 6, "0");
			} else if(numeroConta.length() == 6){
				return numeroConta;
			}
		}
		return null;
	}

	@Override
	public String formatarDigitoConta(String digitoConta) {
		return digitoConta;
	}

}

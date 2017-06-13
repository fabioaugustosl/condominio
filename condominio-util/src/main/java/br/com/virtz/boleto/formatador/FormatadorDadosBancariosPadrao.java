package br.com.virtz.boleto.formatador;


public class FormatadorDadosBancariosPadrao extends FormatadorDadosBancarios {

	@Override
	public String formatarConta(String numeroConta) {
		return numeroConta;
	}

	@Override
	public String formatarDigitoConta(String digitoConta) {
		return digitoConta;
	}

}

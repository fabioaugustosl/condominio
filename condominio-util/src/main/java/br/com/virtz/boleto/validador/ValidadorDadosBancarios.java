package br.com.virtz.boleto.validador;

import java.util.List;

import br.com.virtz.boleto.bean.Conta;

public abstract class ValidadorDadosBancarios {

	public abstract List<String> validar(Conta conta);
	
}

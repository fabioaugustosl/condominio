package br.com.virtz.boleto;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoTitulo;
import br.com.virtz.boleto.bean.NossoNumero;

public interface GeradorNossoNumero {
	public NossoNumero gerar(Conta conta, InfoTitulo titulo);
}

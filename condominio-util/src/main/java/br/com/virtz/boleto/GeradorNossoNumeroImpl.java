package br.com.virtz.boleto;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.EnumBanco;
import br.com.virtz.boleto.bean.InfoTitulo;
import br.com.virtz.boleto.bean.NossoNumero;

public class GeradorNossoNumeroImpl implements GeradorNossoNumero {

	@Override
	public NossoNumero gerar(Conta conta, InfoTitulo titulo) {
		StringBuilder sb = new StringBuilder();
		if(EnumBanco.SANTANDER.getCodigo().equals(conta.getBanco().getCodigoDeCompensacao())){
			sb.append(titulo.getNumeroDocumento());
			return new NossoNumero(StringUtils.leftPad(sb.toString(), 6), "1");
		}
		return new NossoNumero("000000");
	}

}

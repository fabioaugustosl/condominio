package br.com.virtz.boleto;

import java.util.Calendar;

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
			Calendar c = Calendar.getInstance();
			c.setTime(titulo.getDataDocumento());
			sb.append(titulo.getNumeroDocumento());
			sb.append(c.get(Calendar.MONTH));
			sb.append(c.get(Calendar.YEAR));
			if(sb.toString().length() > 12){
				String nn = sb.toString().substring(0, 12);
				return new NossoNumero(nn, "1");
			}
			return new NossoNumero(StringUtils.leftPad(sb.toString(), 12, "0"), "1");
		}
		return new NossoNumero("000000");
	}

}

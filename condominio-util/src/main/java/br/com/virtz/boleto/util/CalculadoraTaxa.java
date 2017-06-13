package br.com.virtz.boleto.util;

import java.math.BigDecimal;
import java.util.List;

import br.com.virtz.boleto.bean.Taxa;

public class CalculadoraTaxa {


	public Double calcular(Double valorOriginal , List<Taxa> taxas){
		BigDecimal v = new BigDecimal(valorOriginal);
		BigDecimal vo = new BigDecimal(valorOriginal);
		if(taxas != null && !taxas.isEmpty()){
			for(Taxa taxa : taxas){
				if(taxa.getValor() != null && taxa.getPorcentagemOuValor() != null){
					BigDecimal valTx = calcularTaxa(taxa, vo);
					v= v.add(valTx);
				}
			}
		}
		
		return tratarValorRetorno(v);
	}
	
	/**
	 * Devolve o valor calculado.
	 * 
	 * @param valorOriginal
	 * @param tx
	 * @return
	 */
	public Double calcular(Double valorOriginal , Taxa tx){
		if(valorOriginal == null){	
			return 0d;
		}
		if(tx.getValor() == null){
			return valorOriginal;
		}
		BigDecimal vo = new BigDecimal(valorOriginal);
		if(tx.getPorcentagemOuValor() != null){
			BigDecimal v = new BigDecimal(tx.getValor());
			BigDecimal valTx = calcularTaxa(tx, vo);
			
			return tratarValorRetorno( vo.add(valTx) );
		}
		return 0d;
	}
	
	
	private Double tratarValorRetorno(BigDecimal ret){
		return ret.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	
	private BigDecimal calcularTaxaPorcentagem(BigDecimal original, BigDecimal valor){
		return original.multiply(valor.divide(new BigDecimal(100d)));
	}
	
	private BigDecimal calcularTaxa(Taxa taxa, BigDecimal original){
		BigDecimal vl =  new BigDecimal(taxa.getValor());
		if("V".equals(taxa.getPorcentagemOuValor())){
			if("D".equals(taxa.getCreditoOuDebito())){
				 return vl.multiply(new BigDecimal(-1d));
			}else {
				 return vl;
			}
		} else {
			BigDecimal porc = calcularTaxaPorcentagem(original, vl);
			if("D".equals(taxa.getCreditoOuDebito())){
				 return porc.multiply(new BigDecimal(-1d));
			} else {
				return porc;
			}
		}
	}
	
}

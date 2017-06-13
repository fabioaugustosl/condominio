package br.com.virtz.condominio.teste;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.virtz.boleto.bean.Taxa;
import br.com.virtz.boleto.util.CalculadoraTaxa;
import br.com.virtz.boleto.util.CalculadoraUtil;

public class TestCalculadoraUtil {

	private CalculadoraTaxa cal = new CalculadoraTaxa();
	private CalculadoraUtil util = new CalculadoraUtil();
	

	@Test
	public void testGerarJuros() {
		List<Taxa> taxas = util.criarTaxasIguaisAutomaticamente(11, "P", "C", 0.2d);
		
		Assert.assertNotNull(taxas);
		Assert.assertEquals(11, taxas.size());
		Assert.assertEquals("P", taxas.get(3).getPorcentagemOuValor());
		Assert.assertEquals("C", taxas.get(4).getCreditoOuDebito());
		Assert.assertEquals(new Double(0.2d), taxas.get(6).getValor());
	}
	
}

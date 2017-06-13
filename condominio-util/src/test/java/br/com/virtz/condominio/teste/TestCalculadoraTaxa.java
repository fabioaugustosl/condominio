package br.com.virtz.condominio.teste;

import junit.framework.Assert;

import org.junit.Test;

import br.com.virtz.boleto.bean.Taxa;
import br.com.virtz.boleto.util.CalculadoraTaxa;

public class TestCalculadoraTaxa {

	private CalculadoraTaxa cal = new CalculadoraTaxa();;
	

	@Test
	public void testSomarValor() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("C");
		t.setPorcentagemOuValor("V");
		t.setValor(10.0d);
		
		// 1
		Double valor = 100.0d;
		Double total = 110.0d;
				
		Double valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		valor = 104.6d;
		total = 107.59d;
		t.setValor(2.99d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
	}
	
	
	@Test
	public void testSubtracaoValor() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("D");
		t.setPorcentagemOuValor("V");
		t.setValor(10.0d);
		
		// 1
		Double valor = 100.0d;
		Double total = 90.0d;
				
		Double valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		valor = 104.6d;
		total = 101.61d;
		t.setValor(2.99d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
	}
	
	
	@Test
	public void testSomaPorcentagem() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("C");
		t.setPorcentagemOuValor("P");
		t.setValor(5.0d);
		
		// 1
		Double valor = 100.0d;
		Double total = 105.0d;
				
		Double valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		valor = 104.6d;
		total = 105.65d;
		t.setValor(1d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		// 3
		valor = 102.0d;
		total = 102.51d;
		t.setValor(0.5d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		
		// 4
		valor = 105.0d;
		total = 106.04d;
		t.setValor(0.99d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
	}
	
	
	@Test
	public void testSubtracaoPorcentagem() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("D");
		t.setPorcentagemOuValor("P");
		t.setValor(5.0d);
		
		// 1
		Double valor = 100.0d;
		Double total = 95.0d;
				
		Double valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		valor = 104.6d;
		total = 103.55d;
		t.setValor(1d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		// 3
		valor = 102.0d;
		total = 101.49d;
		t.setValor(0.5d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
		
		
		// 4
		valor = 105.0d;
		total = 103.96d;
		t.setValor(0.99d);
				
		valorCalculado =  cal.calcular(valor, t);
		Assert.assertEquals(total, valorCalculado);
	}
}

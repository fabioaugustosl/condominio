package br.com.virtz.condominio.teste;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.com.virtz.boleto.bean.Taxa;
import br.com.virtz.boleto.util.CalculadoraTaxa;

public class TestCalculadoraTaxaLista {

	private CalculadoraTaxa cal = new CalculadoraTaxa();
	

	@Test
	public void testSomarValorCom1() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("C");
		t.setPorcentagemOuValor("V");
		t.setValor(10.0d);
		
		List<Taxa> taxas = new ArrayList<Taxa>();
		taxas.add(t);
		
		// 1
		Double valor = 100.0d;
		Double total = 110.0d;
				
		Double valorCalculado =  cal.calcular(valor, taxas);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		valor = 104.6d;
		total = 107.59d;
		t.setValor(2.99d);
				
		valorCalculado =  cal.calcular(valor, taxas);
		Assert.assertEquals(total, valorCalculado);
	}
	
	
	@Test
	public void testCom2Somar() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("C");
		t.setPorcentagemOuValor("V");
		t.setValor(10.0d);
		
		Taxa t1 = new Taxa();
		t1.setCreditoOuDebito("C");
		t1.setPorcentagemOuValor("P");
		t1.setValor(2.0d);
		
		List<Taxa> taxas = new ArrayList<Taxa>();
		taxas.add(t);
		taxas.add(t1);
		
		// 1
		Double valor = 200.0d;
		Double total = 214.0d;
				
		Double valorCalculado =  cal.calcular(valor, taxas);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		total = 228.89d;
		valor = 214.6d;
				
		valorCalculado =  cal.calcular(valor, taxas);
		Assert.assertEquals(total, valorCalculado);
	}
	
	@Test
	public void testCom2SomaSubtracao() {
		Taxa t = new Taxa();
		t.setCreditoOuDebito("C");
		t.setPorcentagemOuValor("V");
		t.setValor(10.0d);
		
		Taxa t1 = new Taxa();
		t1.setCreditoOuDebito("D");
		t1.setPorcentagemOuValor("P");
		t1.setValor(2.0d);
		
		List<Taxa> taxas = new ArrayList<Taxa>();
		taxas.add(t);
		taxas.add(t1);
		
		// 1
		Double valor = 200.0d;
		Double total = 206.0d;
				
		Double valorCalculado =  cal.calcular(valor, taxas);
		Assert.assertEquals(total, valorCalculado);
		
		// 2
		total = 220.31d;
		valor = 214.6d;
				
		valorCalculado =  cal.calcular(valor, taxas);
		Assert.assertEquals(total, valorCalculado);
	}
	

}

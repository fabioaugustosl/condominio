package br.com.virtz.condominio.teste;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.virtz.boleto.util.FormatadorTexto;

public class TestQuebrarTexto {

	FormatadorTexto formata = new FormatadorTexto();

	@Test
	public void testQuebrarTextoComBrs() {
		String txt = "Olá.<br>Meu nome é Fabio e sou estou fazendo um teste de quebra de linha com brs.<br /> Esse teste é bem interessante.<br/>Talvez funcione bem.";
		List<String> linhas = formata.quebrarStrings(txt,999);
		Assert.assertEquals(4, linhas.size());
	}
	
	
	@Test
	public void testQuebrarTextoComBarraN() {
		String txt = "Olá.\nMeu nome é Fabio e sou estou fazendo um teste de quebra de linha com brs.\n Esse teste é bem interessante.\nTalvez funcione bem.";
		List<String> linhas = formata.quebrarStrings(txt,999);
		Assert.assertEquals(4, linhas.size());
	}
	
	
	@Test
	public void testQuebrarTextoComBrsTamanhoMax50() {
		String txt = "Olá.<br>Meu nome é Fabio e sou estou fazendo um teste de quebra de linha com brs. Esse teste é bem interessante.<br/>Talvez funcione bem.";
		List<String> linhas = formata.quebrarStrings(txt,50);
		Assert.assertEquals(5, linhas.size());
		Assert.assertEquals("Olá.", linhas.get(0));
		Assert.assertEquals("Meu nome é Fabio e sou estou fazendo um teste de", linhas.get(1));
		Assert.assertEquals("quebra de linha com brs. Esse teste é bem", linhas.get(2));
		Assert.assertEquals("interessante.", linhas.get(3));
		Assert.assertEquals("Talvez funcione bem.", linhas.get(4));
		
		
		
	}
}

package br.com.virtz.html;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.virtz.condominio.util.LimparTagHtml;

public class LimparTagsHtmlTest {

	@Test
	public void testLimparTagHtml() {
		String textoTags = getTextTags();
		String textoSemTags = getTextNoTags();
		
		LimparTagHtml limpador = new LimparTagHtml();
		
		assertEquals(textoSemTags, limpador.limpar(textoTags));
	}
	
	

	private String getTextTags() {
		StringBuilder sb = new StringBuilder();
		sb.append("Este é um exemplo de texto.<br/> ");
		sb.append(" <b>Negrito</b>  <div> Podem conter divs </div> ");
		sb.append(" <span styla='display:none'>Spam</span> ");
		sb.append(" <table><tr><td>vai</td><td>vem</td></tr></table> ");
		sb.append(" <img src='bla/teste.jpg'></img> ");
		return sb.toString();
	}
	
	private String getTextNoTags() {
		StringBuilder sb = new StringBuilder();
		sb.append("Este é um exemplo de texto. ");
		sb.append(" Negrito   Podem conter divs  ");
		sb.append(" Spam ");
		sb.append(" vaivem ");
		sb.append("  ");
		return sb.toString();
	}
}

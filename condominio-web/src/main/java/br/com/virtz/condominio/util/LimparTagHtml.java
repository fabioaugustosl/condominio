package br.com.virtz.condominio.util;

public class LimparTagHtml {

	public String limpar(String textWithTag){
		return textWithTag.replaceAll("\\<.*?>","");
	}
}

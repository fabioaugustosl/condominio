package br.com.virtz.boleto;

import java.io.File;

import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;

public class ConversorBoletoPdf {

	public File converter(Boleto boleto, String nomeArquivo){
	
		BoletoViewer boletoViewer = new BoletoViewer(boleto);
		File arquivoPdf = boletoViewer.getPdfAsFile(nomeArquivo+".pdf");
		
		return arquivoPdf;
	}
}

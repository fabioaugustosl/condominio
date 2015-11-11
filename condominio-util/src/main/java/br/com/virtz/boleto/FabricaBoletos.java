package br.com.virtz.boleto;

import java.io.File;

import org.jrimum.bopepo.Boleto;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;

public class FabricaBoletos implements IFabricaBoletos {

	private GeradorBoleto gerador = null; 
	private ConversorBoletoPdf conversor = null;
	
	@Override
	public Boleto geraBoleto(InfoCedente cedente, Conta conta, InfoSacado sacado, InfoTitulo titulo) {
		gerador = new GeradorBoleto();
		conversor = new ConversorBoletoPdf();
		
		Boleto boleto = gerador.gerar(cedente, conta, sacado, titulo);
		return boleto;
		
	}
	
	@Override
	public File boletoToFile(Boleto boleto) {
		
		File arqBoleto = conversor.converter(boleto, "boleto");
		return arqBoleto;
	}

}

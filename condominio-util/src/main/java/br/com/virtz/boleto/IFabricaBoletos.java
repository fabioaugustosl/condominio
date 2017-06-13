package br.com.virtz.boleto;

import java.io.File;

import org.jrimum.bopepo.Boleto;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;

public interface IFabricaBoletos {

	public Boleto geraBoleto(InfoCedente infoCedente, Conta conta, InfoSacado infoSacado, InfoTitulo infoTitulo);
	public File boletoToFile(Boleto boleto);
	
}

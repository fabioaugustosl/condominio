package br.com.virtz.boleto.util;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.boleto.bean.Taxa;

public class CalculadoraUtil {

	public List<Taxa> criarTaxasIguaisAutomaticamente(int total, String tipo, String credidoDebito, Double valor){
		 List<Taxa> taxas = new ArrayList<Taxa>();
		 
		 if(total <= 0){
			 return taxas;
		 }
		 if(tipo == null){
			 tipo = "P";
		 }
		 if(credidoDebito == null){
			 credidoDebito = "C";
		 }
		 
		 while(total>0){
			 total--;
			 Taxa t = new Taxa();
			 t.setCreditoOuDebito(credidoDebito);
			 t.setDescricao("Juros");
			 t.setPorcentagemOuValor(tipo);
			 t.setValor(valor);
			 taxas.add(t);
		 }
		 
		 return taxas;
	}
	
}

package br.com.virtz.boleto.validador;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.boleto.bean.Conta;

public class ValidadorDadosBancariosSantander extends ValidadorDadosBancarios {

	@Override
	public List<String> validar(Conta conta) {
		List<String> erros = new ArrayList<String>();
		if(conta == null){
			erros.add("Não existe conta.");
		} else {
			if(conta.getBanco() == null){
				erros.add("Banco não preenchido.");
			}
			if(conta.getNumeroConta() == null) {
				erros.add("Número da conta não preenchido.");
			}
			if(conta.getDigitoVerificadorConta() == null) {
				erros.add("Dígito da conta não preenchido.");
			}
			if(conta.getDigitoVerificadorConta() != null && conta.getDigitoVerificadorConta().toString().length() != 1) {
				erros.add("Dígito da conta bancária deve ter 1 caracter.");
			}
			if(conta.getBanco() == null){
				erros.add("Banco não preenchido.");
			}
			if(conta.getNumeroAgencia() == null){
				erros.add("Agência bancária não preenchido.");
			}
			if(conta.getCodigoCarteira() != null && 
					(!conta.getCodigoCarteira().equals(Integer.parseInt("101")) && !conta.getCodigoCarteira().equals(Integer.parseInt("200")) && !conta.getCodigoCarteira().equals(Integer.parseInt("201")))){
				erros.add("O código da carteira deve ser 101, 200 ou 201.");
			}
		}
		return erros.isEmpty() ? null : erros;
	}

	
}

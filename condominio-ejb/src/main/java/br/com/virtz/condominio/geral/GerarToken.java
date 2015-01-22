package br.com.virtz.condominio.geral;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.ejb.Stateless;

import br.com.virtz.condominio.entidades.Token;

@Stateless
public class GerarToken implements IGerarToken{

	private String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Z"};
	
	@Override
	public Token gerar(){
		return gerar(null);
	}

	@Override
	public Token gerar(Date dataExpiracao) {
		Token t = new Token();
		
		t.setToken(gerarToken());
		t.setData(new Date());
		t.setDataExpiracao(dataExpiracao);
		
		return t;
	}
	
	private String gerarToken(){
		Random gerador = new Random();
			
		StringBuilder sb = new StringBuilder();
		sb.append(letras[gerador.nextInt(letras.length-1)]);
		sb.append(letras[gerador.nextInt(letras.length-1)]);
		sb.append(letras[gerador.nextInt(letras.length-1)]);
		sb.append(Calendar.getInstance().getTimeInMillis());
		
		return sb.toString();
	}
	
}

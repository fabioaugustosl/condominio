package br.com.virtz.condominio.geral;

import java.util.Date;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Token;

@Local
public interface IGerarToken {
	public Token gerar();
	public Token gerar(Date dataExpiracao);
}

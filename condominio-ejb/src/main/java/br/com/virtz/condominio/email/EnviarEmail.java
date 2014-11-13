package br.com.virtz.condominio.email;

import javax.ejb.Local;

import br.com.virtz.condominio.bean.Email;

@Local
public interface EnviarEmail {
	public boolean enviar(Email email);
}

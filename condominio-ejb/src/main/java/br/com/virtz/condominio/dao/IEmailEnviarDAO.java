package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.EmailParaEnviar;

@Local
public interface IEmailEnviarDAO extends CrudDAO<EmailParaEnviar> {
	public List<EmailParaEnviar> recuperarnaoEnviados();
}

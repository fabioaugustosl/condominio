package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.EmailParaEnviar;
import br.com.virtz.condominio.entidades.MensagemSindico;

@Stateless
public class EmailEnviarDAO extends DAO<EmailParaEnviar> implements IEmailEnviarDAO {

	@Override
	public List<EmailParaEnviar> recuperarnaoEnviados() {

		Query qry = getEntityManager().createNamedQuery("EmailParaEnviar.recuperarNaoEnviados");
		qry.setMaxResults(300);

		return qry.getResultList();
	}

}

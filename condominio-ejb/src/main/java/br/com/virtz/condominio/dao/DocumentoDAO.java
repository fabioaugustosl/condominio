package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Documento;

@Stateless
public class DocumentoDAO extends DAO<Documento> implements IDocumentoDAO {

	@Override
	public List<Documento> recuperar(Condominio condominio) {
		Query qry = getEntityManager().createNamedQuery("Documento.recuperarPorCondominio");
		qry.setParameter("idCondominio", condominio.getId());
		return qry.getResultList();
	}

}

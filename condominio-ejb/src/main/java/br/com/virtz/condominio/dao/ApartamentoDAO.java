package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Apartamento;

@Stateless
public class ApartamentoDAO extends DAO<Apartamento> implements IApartamentoDAO {

	@Override
	public Apartamento recuperarPorNumero(Long idCondominio, String numeroApto, String nomeBloco) {

		Query qry = getEntityManager().createNamedQuery("Apartamento.recuperarPorCondominioENumeroBloco");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("numero", numeroApto);
		qry.setParameter("nomeBloco", nomeBloco);
		List<Apartamento> aptos = qry.getResultList();
		
		if(aptos != null && !aptos.isEmpty()){
			return aptos.get(0);
		}
		return null;
		
	}
	
	
	
}

package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.ApartamentoExtraUsuario;

@Stateless
public class ApartamentoExtraUsuarioDAO extends DAO<ApartamentoExtraUsuario> implements IApartamentoExtraUsuarioDAO {

	@Override
	public List<ApartamentoExtraUsuario> recuperar(Long idUsuario) {
		
		if(idUsuario == null){
			return null;
		}
		
		Query qry = getEntityManager().createNamedQuery("ApartamentoExtraUsuario.recuperarPorUsuario");
		qry.setParameter("idUsuario", idUsuario);
		qry.setMaxResults(300);
		
		return qry.getResultList();
	}

	@Override
	public void removerTodos(Long idUsuario) {
		Query qry = getEntityManager().createQuery("delete from ApartamentoExtraUsuario where usuario.id = "+idUsuario);
		qry.executeUpdate();
	}

}

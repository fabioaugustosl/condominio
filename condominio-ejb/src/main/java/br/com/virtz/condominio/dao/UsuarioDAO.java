package br.com.virtz.condominio.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.entidades.Usuario;

@Stateless
public class UsuarioDAO extends DAO<Usuario> implements IUsuarioDAO {

	@Override
	public Usuario recuperarUsuarioCompleto(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT u FROM Usuario u ")
		.append(" JOIN FETCH u.condominio c ")
		.append(" JOIN FETCH c.areasComuns areas ")
		.append(" WHERE u.id = :idUsuario ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		qry.setParameter("idUsuario", id);
		return (Usuario) qry.getSingleResult();
	}
	
}

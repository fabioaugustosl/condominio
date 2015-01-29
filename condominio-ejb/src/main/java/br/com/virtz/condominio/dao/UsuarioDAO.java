package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
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
	

	@Override
	public List<Usuario> recuperarTodos(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Usuario.recuperarPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	
	@Override
	public void alterarStatus(Long idUsuario, EnumTipoUsuario tipoUsuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE Usuario u ")
		.append(" SET u.tipoUsuario = :tipoUsuairo ")
		.append(" WHERE u.id = :idUsuario ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		
		qry.setParameter("idUsuario", idUsuario);
		qry.setParameter("tipoUsuairo", tipoUsuario);
		
		qry.executeUpdate();
	}
	
}

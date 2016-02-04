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
		sb.append("SELECT u FROM ").append(Usuario.class.getName()).append(" u ")
		.append(" LEFT JOIN FETCH u.condominio c ")
		.append(" LEFT JOIN FETCH c.areasComuns areas ")
		.append(" WHERE u.id = :idUsuario AND u.deletado = 0 ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		qry.setParameter("idUsuario", id);
		List<Usuario> usuarios = qry.getResultList();
		if(usuarios != null && !usuarios.isEmpty()){
			return usuarios.get(0);
		}
		return null;
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
		sb.append("UPDATE ").append(Usuario.class.getName()).append(" u ")
		.append(" SET u.tipoUsuario = :tipoUsuairo ")
		.append(" WHERE u.id = :idUsuario AND u.deletado = 0 ");
		
		Query qry = getEntityManager().createQuery(sb.toString());
		
		qry.setParameter("idUsuario", idUsuario);
		qry.setParameter("tipoUsuairo", tipoUsuario);
		
		qry.executeUpdate();
	}


	@Override
	public Usuario recuperarUsuarioPorEmail(String email) {
		Query qry = getEntityManager().createNamedQuery("Usuario.recuperarPorEmail");
		qry.setParameter("email", email);
		List<Usuario> usuarios = qry.getResultList();
		if(usuarios != null && !usuarios.isEmpty()){
			return usuarios.get(0);
		}
		return null;
	}
	
	@Override
	public List<Usuario> recuperarUsuariosPorEmail(String email) {
		Query qry = getEntityManager().createNamedQuery("Usuario.recuperarUsuariosPorEmail");
		qry.setParameter("email", email);
		return qry.getResultList();
	}


	@Override
	public List<Usuario> recuperarSindicos(Long idCondominio) {
		Query qry = getEntityManager().createNamedQuery("Usuario.recuperarSindicosPorCondominio");
		qry.setParameter("idCondominio", idCondominio);
		return qry.getResultList();
	}
	
	
	@Override
	public List<Usuario> recuperar(Long idCondominio, EnumTipoUsuario tipo) {
		Query qry = getEntityManager().createNamedQuery("Usuario.recuperarPorCondominioETipoUsuario");
		qry.setParameter("idCondominio", idCondominio);
		qry.setParameter("tipoUsuario", tipo);
		return qry.getResultList();
	}
	
	
	@Override
	public void remover(Long id)  {
		Usuario u = recuperarPorId(id);
		if(u != null){
			u.setEmail("");
			u.setDeletado(Boolean.TRUE);
			try {
				salvar(u);
			} catch (Exception e) {
				super.remover(id);
			}
		}
	}


	@Override
	public Usuario recuperarUsuarioPorEmailAutenticacao(String email) {
		Query qry = getEntityManager().createNamedQuery("Usuario.recuperarPorEmailAutenticacao");
		qry.setParameter("email", email);
		List<Usuario> usuarios = qry.getResultList();
		if(usuarios != null && !usuarios.isEmpty()){
			return usuarios.get(0);
		}
		return null;
	}
	
}

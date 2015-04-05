package br.com.virtz.condominio.util;

import javax.ejb.EJB;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;

import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.Usuario;

public class MeuRealm extends AuthenticatingRealm {

	private IUsuarioDAO dao;
	 
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		String principal = (String) token.getPrincipal();
		 
		 InitialContext context;
		try {
			context = new InitialContext();
			BeanManager  bm = (BeanManager)context.lookup("java:comp/BeanManager");
			Bean bean = bm.getBeans(IUsuarioDAO.class).iterator().next();
			//Bean bean = bm.getBeans("IUsuarioDAO").iterator().next();
		    CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
		    dao = (IUsuarioDAO) bm.getReference(bean, IUsuarioDAO.class, ctx);
		} catch (NamingException e) {
			e.printStackTrace();
			throw new AuthenticationException("Ocorreu erro ao processar a autenticação do usuário.");
		}
		 
        Usuario credencial = dao.recuperarUsuarioPorEmail(principal);
 
        if(credencial != null) {
             AuthenticationInfo info = new SimpleAuthenticationInfo(principal,credencial.getSenha() , "Usuario");
             SimpleCredentialsMatcher cmatcher = new SimpleCredentialsMatcher();
 
             boolean credentialsMatch = cmatcher.doCredentialsMatch(token, info);
             if(credentialsMatch) {
                 return info;
             }
 
         }
         throw new AuthenticationException();
	}

}

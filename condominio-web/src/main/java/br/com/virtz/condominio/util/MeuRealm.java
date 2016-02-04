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
		 
        Usuario credencial = dao.recuperarUsuarioPorEmailAutenticacao(principal);
 
        if(credencial != null) {
        	
        	if(!credencial.getCadastroConfirmado()){
        		throw new AuthenticationException("Usuário não confirmado. Acesse seu email para finalizar seu cadastro.");
        	}
        	if(credencial.getCondominio() == null){
        		throw new AuthenticationException("Condomínio não reconhecido. Favor enviar um email para contato@condominiosobcontrole.com.br com seu nome, condomínio e seu apto. Corrigiremos seu cadastro o mais rápido possível.");
        	}
            AuthenticationInfo info = new SimpleAuthenticationInfo(principal, credencial.getSenha() , "usuario");
            SimpleCredentialsMatcher cmatcher = new SimpleCredentialsMatcher();
 
            boolean credentialsMatch = cmatcher.doCredentialsMatch(token, info);
            if(credentialsMatch) {
                return info;
            }
 
         }
         throw new AuthenticationException("Usuário ou senha inválido(s)");
	}

}

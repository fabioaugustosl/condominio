package br.com.csc.rest;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.geral.CriptografarSenha;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IUsuarioService;


@Path("/usuario")
public class UsuarioResource {

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private ICondominioService condominioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> getPorCondominio(@QueryParam("idCondominio") Long idCondominio) {
    	List<Usuario> usuarios = null;
    	if(idCondominio != null){
    		Condominio condominio = new Condominio();
    		condominio.setId(idCondominio);
    		usuarios = usuarioService.recuperarTodos(condominio);
    	} else {
    		usuarios = usuarioService.recuperarTodos();
    	}
    	if(usuarios != null && !usuarios.isEmpty()){
    		for(Usuario u : usuarios){
    			preparaUsuarioParaRetorno(u);
    		}
    	}
    	return usuarios;
    }


    @GET
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario getPorEmail(@PathParam("email") String email) {
    	Usuario u = usuarioService.recuperarUsuario(email);
    	preparaUsuarioParaRetorno(u);
    	return u;
    }




    @GET
    @Path("/login/{email}/{senha}")
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean usuarioValido(@PathParam("email") String email, @PathParam("senha") String senha) {
        Usuario u = usuarioService.recuperarUsuario(email);
        if(u != null && senha != null && !u.getCondominio().getBloqueado() &&
        		(EnumTipoUsuario.MORADOR.equals(u.getTipoUsuario()) || EnumTipoUsuario.SINDICO.equals(u.getTipoUsuario()) || EnumTipoUsuario.ADMINISTRATIVO.equals(u.getTipoUsuario()) )){
        	CriptografarSenha criptografar = new CriptografarSenha();
        	try {
    			senha = criptografar.hash256(senha);
    		} catch (NoSuchAlgorithmException e) {}
        	return senha.equals(u.getSenha());
        }
        return Boolean.FALSE;
    }




    protected void preparaUsuarioParaRetorno(Usuario u) {
    	if(u == null){
    		return;
    	}
    	Condominio c = new Condominio();
    	c.setId(u.getCondominio().getId());
    	c.setNome(u.getCondominio().getNome());
		u.setCondominio(c);
    	u.getApartamento().setUsuario(null);
    	if(u.getApartamento().getBloco() != null){
    		u.getApartamento().getBloco().setApartamentos(null);
    		u.getApartamento().getBloco().setAgrupamentoUnidades(null);
    		u.getApartamento().getBloco().setCondominio(null);
    	}
    	u.setUnidade(null);
	}


}

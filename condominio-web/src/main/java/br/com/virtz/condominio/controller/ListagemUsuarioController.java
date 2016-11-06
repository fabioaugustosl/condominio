package br.com.virtz.condominio.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemUsuarioController {

	@EJB
	private IUsuarioService usuarioService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;

	private List<Usuario> usuarios;
	private Usuario usuarioSelecionado = null;
	private BloqueioFuncaoUsuario bloqueioUsuarioSelecionado = null;
	private List<BloqueioFuncaoUsuario> usuariosBloqueados = null;

	Usuario usuario = null;

	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		usuarios = listarTodos();
		usuariosBloqueados = usuarioService.recuperarBloqueiosPorCondominio(usuario.getCondominio().getId(), EnumFuncaoBloqueio.RESERVA);
	}


	public List<Usuario> listarTodos(){

		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());

		return usuarios;
	}


	public void remover(Usuario usuario) throws CondominioException {
		usuarioService.remover(usuario.getId());
		usuarios.remove(usuario);
		messageHelper.addInfo("Usuário removido com sucesso!");
	}


	public void alterarParaSindico(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaSindico(usuario.getId());
			alterarTipoUsuarioListaCarregada(usuario, EnumTipoUsuario.SINDICO);
			messageHelper.addInfo("Usuário alterado para Síndico!");
		}
	}

	public void alterarParaAdministrativo(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaAdministrativo(usuario.getId());
			alterarTipoUsuarioListaCarregada(usuario, EnumTipoUsuario.ADMINISTRATIVO);
			messageHelper.addInfo("Usuário alterado para Administrativo!");
		}
	}

	public void alterarParaMorador(Usuario usuario) throws CondominioException {
		if(usuario != null){
			usuarioService.alterarParaMorador(usuario.getId());
			alterarTipoUsuarioListaCarregada(usuario, EnumTipoUsuario.MORADOR);
			messageHelper.addInfo("Usuário alterado para Morador!");
		}
	}

	private void alterarTipoUsuarioListaCarregada(Usuario usuario, EnumTipoUsuario tipo){
		for(Usuario u : usuarios){
			if(u.equals(usuario)){
				u.setTipoUsuario(tipo);
				break;
			}
		}
	}


	public void montarInfoTelaBloqueio(Usuario usuario){
		if(usuario == null){
			return;
		}
		bloqueioUsuarioSelecionado = null;
		this.usuarioSelecionado = usuario;
		List<BloqueioFuncaoUsuario> bloqueios = usuarioService.recuperarBloqueios(usuario.getId());
		if(bloqueios != null && !bloqueios.isEmpty()){
			bloqueioUsuarioSelecionado = bloqueios.get(0);
		} else {
			bloqueioUsuarioSelecionado = new BloqueioFuncaoUsuario();
			bloqueioUsuarioSelecionado.setUsuario(usuarioSelecionado);
			bloqueioUsuarioSelecionado.setFuncaoBloqueio(EnumFuncaoBloqueio.RESERVA);
			bloqueioUsuarioSelecionado.setComentario(EnumFuncaoBloqueio.RESERVA.getMsgParaUsuario());
		}
	}


	public void limparaInfoTelaBloqueio(){
		usuarioSelecionado = null;
		bloqueioUsuarioSelecionado = null;
	}


	public void salvarBloqueioUsuario(){
		if(bloqueioUsuarioSelecionado != null){
			if(!bloqueioUsuarioSelecionado.isMarcado()){
				usuarioService.removerBloqueio(bloqueioUsuarioSelecionado.getUsuario().getId(), bloqueioUsuarioSelecionado.getFuncaoBloqueio());
				messageHelper.addInfo("Bloqueio retirado.");
			} else {
				try {
					usuarioService.bloquearFuncao(bloqueioUsuarioSelecionado.getUsuario(), bloqueioUsuarioSelecionado.getFuncaoBloqueio(), bloqueioUsuarioSelecionado.getComentario());
					messageHelper.addInfo("Bloqueio confirmado.");
				} catch (Exception e) {
					e.printStackTrace();
					messageHelper.addError("Ocorreu um erro ao bloquear funcionalidade para este usuário.");
				}
			}

			usuariosBloqueados = usuarioService.recuperarBloqueiosPorCondominio(usuarioSelecionado.getCondominio().getId(), EnumFuncaoBloqueio.RESERVA);
			limparaInfoTelaBloqueio();
		}
	}


	public boolean usuarioPossuiBloqueios(Usuario usuario){

		if(usuariosBloqueados == null || usuariosBloqueados.isEmpty()){
			return false;
		}
		for(BloqueioFuncaoUsuario bloqueio : usuariosBloqueados){
			if(bloqueio.getUsuario().getId().equals(usuario.getId())){
				return true;
			}
		}

		return false;
	}

	public void changeValueBloqueio(){
		/*if(bloqueioUsuarioSelecionado != null){
			bloqueioUsuarioSelecionado.setMarcado(!bloqueioUsuarioSelecionado.isMarcado());
		}*/
	}


	// GETTERS E SETTERS
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public BloqueioFuncaoUsuario getBloqueioUsuarioSelecionado() {
		return bloqueioUsuarioSelecionado;
	}


}

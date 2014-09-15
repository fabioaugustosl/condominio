package br.com.virtz.condominio.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import br.com.virtz.condominio.entity.Usuario;

@SessionScoped
public class SessaoUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuarioLogado;

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
		
}

package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoNotificacao;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Notificacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface INotificacaoService {
	public List<Notificacao> recuperar(Long idCondominio, Long idUsuario);
	public void salvarNotificacao(Notificacao notificacao) throws AppException ;
	public void salvarNovaNotificacao(Condominio condominio, Usuario usuario, EnumTipoNotificacao tipo, String texto) throws AppException ;
	public void remover(Long idNotificacao) throws AppException;
}


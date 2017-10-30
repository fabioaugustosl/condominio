package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.EmailParaEnviar;

@Local
public interface IEmailEnviarService {
	public EmailParaEnviar salvar(EmailParaEnviar mensagem) throws Exception;
	public void remover(Long id);
	public List<EmailParaEnviar> recuperarNaoEnviados();

}

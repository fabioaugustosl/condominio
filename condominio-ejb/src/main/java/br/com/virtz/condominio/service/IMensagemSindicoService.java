package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.RespostaMensagemSindico;

@Local
public interface IMensagemSindicoService {
	public MensagemSindico salvar(MensagemSindico mensagem) throws Exception;
	public void remover(Long id);
	public MensagemSindico recuperarMensagemSindico(Long id);
	public List<MensagemSindico> recuperarTodos(Long idCondominio);
	
	// resposta
	public RespostaMensagemSindico salvar(RespostaMensagemSindico resposta) throws Exception;
	public RespostaMensagemSindico recuperarUltimaResposta(Long idMensagem);
}

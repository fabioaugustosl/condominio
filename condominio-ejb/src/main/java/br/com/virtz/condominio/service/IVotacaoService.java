package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IVotacaoService {
	
	public Votacao criarNovaVotacao(Usuario usuario, Condominio condominio, EnumTipoVotacao tipoVotacao, String assunto);
	public boolean salvarVotacao(Votacao votacao);
	public boolean votar(Voto voto);
	public Votacao buscar(Long idVotacao);
	public void removerOpcao(OpcaoVotacao opcao);
	public List<Votacao> recuperarTodas(Condominio condominio);
	public void ativarVotacao(Votacao votacao) throws AppException;
	public void desativarVotacao(Votacao votacao) throws AppException;
	public void removerVotacao(Votacao votacao) throws AppException;
	
}


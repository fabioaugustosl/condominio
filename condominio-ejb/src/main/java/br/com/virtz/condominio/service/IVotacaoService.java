package br.com.virtz.condominio.service;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;

@Local
public interface IVotacaoService {
	
	public Votacao criarNovaVotacao(Usuario usuario, Condominio condominio, EnumTipoVotacao tipoVotacao, String assunto);
	public boolean salvarVotacao(Votacao votacao);
	public boolean votar(Voto voto);
	public Votacao buscar(Long idVotacao);
	
}

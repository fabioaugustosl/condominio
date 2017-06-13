package br.com.virtz.condominio.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacaoComImagem;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ParametroObrigatorioNuloException;

@Local
public interface IVotacaoService {

	public Votacao criarNovaVotacao(Usuario usuario, Condominio condominio, EnumTipoVotacao tipoVotacao, String assunto);
	public boolean salvarVotacao(Votacao votacao);
	public boolean votar(Voto voto) throws AppException;
	public Votacao buscar(Long idVotacao);
	public void removerOpcao(OpcaoVotacao opcao);
	public List<Votacao> recuperarTodas(Condominio condominio);
	public List<Votacao> recuperarTodasAtivas(Condominio condominio);
	public void ativarVotacao(Votacao votacao) throws AppException;
	public void desativarVotacao(Votacao votacao) throws AppException;
	public void encerrarVotacao(Long idVotacao) throws AppException;
	public void removerVotacao(Votacao votacao) throws AppException;
	public Integer totalVotos(Votacao votacao);
	public Voto recuperarVotoPorUsuario(Votacao votacao, Usuario usuario) throws ParametroObrigatorioNuloException;
	public OpcaoVotacao recuperarOpcao(Long idOpcao);
	public void validarSeUsuarioPodeVotar(Votacao votacao, Usuario usuario) throws AppException;
	public Map<String, Integer> recuperarResultado(Long idVotacao) throws AppException;
	public Voto recuperarVotoPorApto(Votacao votacao, Usuario usuario) throws ParametroObrigatorioNuloException;
	public List<Voto> recuperarTodosVotos(Long idVotacao) throws AppException;
	public void marcarEmailJaEnviado(Long idVotacao) throws AppException;
	public List<Votacao> recuperarVotacoesEncerradasSemEnvioDeEmail();
	public List<Votacao> recuperarVotacoesNovasSemEnvioDeEmail();
	public void removerOpcaoImagem(OpcaoVotacaoComImagem opcao);
	public void marcarEmailNovaVotacaoJaEnviado(Long idVotacao) throws AppException;

}


package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.bean.ResultadoVotacao;
import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.dao.IOpcaoVotacaoComImagemDAO;
import br.com.virtz.condominio.dao.IOpcaoVotacaoDAO;
import br.com.virtz.condominio.dao.IVotacaoDAO;
import br.com.virtz.condominio.dao.IVotoDAO;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacaoComImagem;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ParametroObrigatorioNuloException;

@Stateless
public class VotacaoService implements IVotacaoService {

	@EJB
	private IVotacaoDAO votacaoDAO;

	@EJB
	private IVotoDAO votoDAO;

	@EJB
	private IOpcaoVotacaoDAO opcaoDAO;

	@EJB
	private IOpcaoVotacaoComImagemDAO opcaoImagemDAO;


	@Override
	public Votacao criarNovaVotacao(Usuario usuario, Condominio condominio, EnumTipoVotacao tipoVotacao, String assunto) {
		Votacao v = new Votacao();
		v.setUsuario(usuario);
		v.setCondominio(condominio);
		v.setTipoVotacao(tipoVotacao);
		v.setAssuntoVotacao(assunto);
		v.setAtiva(Boolean.TRUE);
		v.setResultadoParcial(Boolean.FALSE);
		v.setVotacaoOficial(Boolean.FALSE);
		return v;
	}


	@Override
	public boolean salvarVotacao(Votacao votacao) {
		try {
			votacaoDAO.salvar(votacao);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public boolean votar(Voto voto) throws AppException {
		// verifica os campos obrigatórios
		if (voto == null || voto.getUsuario() == null || voto.getVotacao() == null){
			throw new AppException("Dados faltantes para computar seu voto. Fazer tentar novamente.");
		}

		// verifica se o usuário já votou
		Voto v = votoDAO.recuperarPorUsuario(voto.getVotacao(), voto.getUsuario());
		if(v != null){
			throw new AppException("Você já votou!");
		}

		try {
			votoDAO.salvar(voto);
			return true;
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro genérico ao computar o voto.");
		}
	}


	@Override
	public Votacao buscar(Long idVotacao) {
		return votacaoDAO.recuperarPorId(idVotacao);
	}


	@Override
	public void removerOpcao(OpcaoVotacao opcao) {
		if(opcao != null &&  opcao.getId() != null){
			opcaoDAO.remover(opcao.getId());
		}
	}

	@Override
	public void removerOpcaoImagem(OpcaoVotacaoComImagem opcao) {
		if(opcao != null &&  opcao.getId() != null){
			opcaoImagemDAO.remover(opcao.getId());
		}
	}


	@Override
	public List<Votacao> recuperarTodas(Condominio condominio) {
		if(condominio == null){
			return new ArrayList<Votacao>();
		}
		List<Votacao> votacoes =  votacaoDAO.recuperar(condominio);
		Collections.sort(votacoes);
		return votacoes;
	}


	@Override
	public void ativarVotacao(Votacao votacao) throws AppException{
		if(votacao == null || votacao.getId() == null){
			throw new AppException("Erro na identificação da ativação.");
		}

		Date hoje = new Date();
		if(votacao.getDataLimite() != null && hoje.after(votacao.getDataLimite())){
			throw new AppException("Essa votação já expirou e por isso não pode ser ativada.");
		}

		votacao.setAtiva(Boolean.TRUE);
		try{
			this.salvarVotacao(votacao);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro salvar");
		}
	}

	@Override
	public void marcarEmailJaEnviado(Long idVotacao) throws AppException{
		if(idVotacao == null ){
			throw new AppException("Erro na identificação da votação.");
		}
		Votacao votacao = votacaoDAO.recuperarPorId(idVotacao);
		if(votacao == null ){
			throw new AppException("Erro na identificação da votação.");
		}

		votacao.setEmailEnviado(Boolean.TRUE);
		try{
			this.salvarVotacao(votacao);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro ao setar que email já foi enviado.");
		}
	}


	@Override
	public void marcarEmailNovaVotacaoJaEnviado(Long idVotacao) throws AppException{
		if(idVotacao == null ){
			throw new AppException("Erro na identificação da votação.");
		}
		Votacao votacao = votacaoDAO.recuperarPorId(idVotacao);
		if(votacao == null ){
			throw new AppException("Erro na identificação da votação.");
		}

		votacao.setEmailNovaEnviado(Boolean.TRUE);
		try{
			this.salvarVotacao(votacao);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro ao setar que email já foi enviado.");
		}
	}


	@Override
	public void desativarVotacao(Votacao votacao) throws AppException{
		if(votacao == null || votacao.getId() == null){
			throw new AppException("Erro na identificação da desativação.");
		}

		Date hoje = new Date();
		if(votacao.getDataLimite() != null && hoje.after(votacao.getDataLimite())){
			throw new AppException("Essa votação já expirou e por isso não pode ser desativada.");
		}

		votacao.setAtiva(Boolean.FALSE);
		try{
			this.salvarVotacao(votacao);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro salvar");
		}
	}


	@Override
	public void removerVotacao(Votacao votacao) throws AppException{
		if(votacao == null || votacao.getId() == null){
			throw new AppException("Erro na identificação da votação para exclusão.");
		}

		// Uma votação não pode ser excluída se estiver encerrada. Ou seja, se a data limite estiver expirada.
		Date hoje = new Date();
		if(hoje.after(votacao.getDataLimite())){
			throw new AppException("Essa votação já expirou e por isso não pode ser excluída.");
		}

		// Uma votação não pode ser excluída após receber votos.
		Long totalVotos = votoDAO.totalVotos(votacao);
		if(totalVotos != null && totalVotos > 0){
			throw new AppException("Essa votação já recebeu votos e por isso não pode ser excluída.");
		}

		try{
			this.votacaoDAO.remover(votacao.getId());
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro salvar");
		}
	}


	@Override
	public Integer totalVotos(Votacao votacao) {
		Long totalVotos = votoDAO.totalVotos(votacao);
		return totalVotos != null ? totalVotos.intValue() : 0;
	}


	@Override
	public List<Votacao> recuperarTodasAtivas(Condominio condominio) {
		if(condominio == null){
			return new ArrayList<Votacao>();
		}
		return votacaoDAO.recuperarVotacoesAtivas(condominio);
	}

	@Override
	public Voto recuperarVotoPorUsuario(Votacao votacao, Usuario usuario) throws ParametroObrigatorioNuloException{
		if(votacao == null || usuario == null){
			throw new ParametroObrigatorioNuloException("Parâmetros obrigatórios para pesquisa nulos.");
		}
		return votoDAO.recuperarPorUsuario(votacao, usuario);
	}


	@Override
	public Voto recuperarVotoPorApto(Votacao votacao, Usuario usuario) throws ParametroObrigatorioNuloException{
		if(votacao == null || usuario == null){
			throw new ParametroObrigatorioNuloException("Parâmetros obrigatórios para pesquisa nulos.");
		}
		return votoDAO.recuperarPorApto(votacao.getId(), usuario.getApartamento().getId());
	}



	@Override
	public OpcaoVotacao recuperarOpcao(Long idOpcao) {
		return opcaoDAO.recuperarPorId(idOpcao);
	}


	@Override
	public void validarSeUsuarioPodeVotar(Votacao votacao, Usuario usuario) throws AppException {
		if(usuario == null || votacao == null){
			throw new AppException("Não foi possível verificar se o usuário pode votar ou não.");
		}

		if(usuario.getApartamento() == null){
			throw new AppException("Não é permitido morador sem apartamento cadastro votar. Favor acesse seu cadastro e informe qual apartamento você mora.");
		}


		Voto v = votoDAO.recuperarPorApto(votacao.getId(), usuario.getApartamento().getId());

		if(v != null){
			throw new AppException("Outro morador do mesmo apartamento já votou. Só é permitido 1 voto por apartamento.");
		}

	}


	@Override
	public Map<String, Integer> recuperarResultado(Long idVotacao)throws AppException {
		Votacao votacao = votacaoDAO.recuperarPorId(idVotacao);
		return getResultadoVotacao(votacao);
	}


	private Map<String, Integer> getResultadoVotacao(Votacao votacao){
		ResultadoVotacao resultado = new ResultadoVotacao(votacao.getTipoVotacao());

		resultado.inicializarResultado(votacao);

		for(Voto v : votacao.getVotos()){
			resultado.contabilizarVoto(v.getOpcaoVotada(votacao.getTipoVotacao()));
		}

		return resultado.resultado();
	}


	@Override
	public List<Voto> recuperarTodosVotos(Long idVotacao) throws AppException{
		return votoDAO.recuperarTodosVotos(idVotacao);
	}


	@Override
	public void encerrarVotacao(Long idVotacao) throws AppException {
		if(idVotacao == null){
			throw new AppException("Id da votação inválido.");
		}
		Votacao votacao = votacaoDAO.recuperarPorId(idVotacao);
		if(votacao == null){
			throw new AppException("Erro na identificação da desativação.");
		}
		votacao.setEncerrada(Boolean.TRUE);
		try{
			this.salvarVotacao(votacao);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro encerrar votação");
		}
	}


	@Override
	public List<Votacao> recuperarVotacoesEncerradasSemEnvioDeEmail() {
		return votacaoDAO.recuperarVotacoesEncerradasSemEnvioDeEmail();
	}


	@Override
	public List<Votacao> recuperarVotacoesNovasSemEnvioDeEmail() {
		return votacaoDAO.recuperarVotacoesNovasSemEnvioDeEmail();
	}

}

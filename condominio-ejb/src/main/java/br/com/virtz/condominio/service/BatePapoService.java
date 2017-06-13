package br.com.virtz.condominio.service;

import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IAvaliacaoBatePapoDAO;
import br.com.virtz.condominio.dao.IBatePapoDAO;
import br.com.virtz.condominio.dao.IComentarioBatePapoDAO;
import br.com.virtz.condominio.entidades.Avaliacao;
import br.com.virtz.condominio.entidades.BatePapo;
import br.com.virtz.condominio.entidades.ComentarioBatePapo;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Stateless
public class BatePapoService implements IBatePapoService {

	@EJB
	private IBatePapoDAO batePapoDAO;
	
	@EJB
	private IAvaliacaoBatePapoDAO avaliacaoDAO;
	
	@EJB
	private IComentarioBatePapoDAO comentarioBatePapoDAO;
	

	@Override
	public BatePapo salvar(BatePapo batePapo) throws Exception {
		return batePapoDAO.salvar(batePapo);
	}

	@Override
	public void remover(Long id) {
		batePapoDAO.remover(id);
	}

	@Override
	public List<BatePapo> recuperarTodos(Condominio condominio) {
		return batePapoDAO.recuperar(condominio);
	}

	@Override
	public Avaliacao avaliarPositivamente(BatePapo papo, Usuario usuario, String comentario) throws ErroAoSalvar {
		Avaliacao avaliacao = new Avaliacao();
		avaliacao.setObservacao(comentario);
		avaliacao.setPositiva(true);
		avaliacao.setUsuario(usuario);
		
		avaliacao = salvarAvaliacao(papo, avaliacao);
		if(papo.getAvaliacoes() == null){
			papo.setAvaliacoes(new HashSet<Avaliacao>());
		}
		papo.getAvaliacoes().add(avaliacao);
		return avaliacao;
	}

	@Override
	public Avaliacao avaliarNegativamente(BatePapo papo, Usuario usuario, String comentario) throws ErroAoSalvar {
		Avaliacao avaliacao = new Avaliacao();
		avaliacao.setObservacao(comentario);
		avaliacao.setPositiva(false);
		avaliacao.setUsuario(usuario);
		
		avaliacao = salvarAvaliacao(papo, avaliacao);
		if(papo.getAvaliacoes() == null){
			papo.setAvaliacoes(new HashSet<Avaliacao>());
		}
		papo.getAvaliacoes().add(avaliacao);
		return avaliacao;
	}
	
	private Avaliacao salvarAvaliacao(BatePapo papo, Avaliacao avaliacao) throws ErroAoSalvar {
		try {
			avaliacao.setBatePapo(papo);
			return avaliacaoDAO.salvar(avaliacao);
		} catch (Exception e) {
			throw new ErroAoSalvar("Ocorrecu um erro ao salva a avalição.", avaliacao);
		}
	}

	@Override
	public boolean usuarioJaAvaliou(BatePapo batePapo, Usuario usuario) {
		if(batePapo == null || usuario == null){
			return Boolean.FALSE;
		}
		Avaliacao avaliacao = avaliacaoDAO.recuperar(batePapo, usuario);
		return avaliacao == null? Boolean.FALSE: Boolean.TRUE;
	}

	@Override
	public List<ComentarioBatePapo> recuperarComentarios(BatePapo batePapo) {
		return comentarioBatePapoDAO.recuperar(batePapo);
	}


}

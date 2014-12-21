package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.dao.IReservaDAO;
import br.com.virtz.condominio.dao.IVotacaoDAO;
import br.com.virtz.condominio.dao.IVotoDAO;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;

@Stateless
public class VotacaoService implements IVotacaoService {

	@EJB
	private IVotacaoDAO votacaoDAO;
	
	@EJB
	private IVotoDAO votoDAO;

	@Override
	public Votacao criarNovaVotacao(Usuario usuario, Condominio condominio, EnumTipoVotacao tipoVotacao, String assunto) {
		Votacao v = new Votacao();
		v.setUsuario(usuario);
		v.setCondominio(condominio);
		v.setTipoVotacao(tipoVotacao);
		v.setAssuntoVotacao(assunto);
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
	public boolean votar(Voto voto) {
		try {
			votoDAO.salvar(voto);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Votacao buscar(Long idVotacao) {
		return votacaoDAO.rcuperarPorId(idVotacao);
	}

	
	
}

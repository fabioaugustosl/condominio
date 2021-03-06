package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IVisitanteDAO;
import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class VisitanteService implements IVisitanteService {

	@EJB
	private IVisitanteDAO visitanteDAO;



	@Override
	public void remover(Long idVisitante) throws AppException {
		if(idVisitante == null ){
			throw new AppException("Erro na identificação do visitante para exclusão.");
		}

		try{
			this.visitanteDAO.remover(idVisitante);
		} catch(Exception e){
			e.printStackTrace();
			throw new AppException("Erro ao remover o visitante.");
		}
	}


	@Override
	public List<Visitante> recuperar(Long idCondominio) {
		if(idCondominio == null){
			return new ArrayList<Visitante>();
		}
		return visitanteDAO.recuperar(idCondominio);
	}


	@Override
	public void salvarVisitante(Visitante visitante) throws AppException {
		try {
			visitanteDAO.salvar(visitante);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar o visitante.");
		}
	}


	@Override
	public int totalVisitantes(Long idCondominio) {
		return visitanteDAO.totalVisitantes(idCondominio);
	}


	@Override
	public List<Visitante> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		return visitanteDAO.recuperarPaginado(idCondominio, inicio, qtdRegistros);
	}

	@Override
	public int total(Long idCondominio) {
		return visitanteDAO.totalVisitantes(idCondominio);
	}


	@Override
	public void saidaVisitante(Long idVisitante) throws AppException {
		Visitante v = visitanteDAO.recuperarPorId(idVisitante);
		v.setDataSaida(new Date());
		try {
			visitanteDAO.salvar(v);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}


}

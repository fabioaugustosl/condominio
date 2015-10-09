package br.com.virtz.condominio.service;

import java.util.ArrayList;
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
	

}

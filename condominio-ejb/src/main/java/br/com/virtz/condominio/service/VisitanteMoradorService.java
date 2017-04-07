package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IVisitanteDAO;
import br.com.virtz.condominio.entidades.Visitante;

@Stateless
public class VisitanteMoradorService implements PaginacaoServiceVisitantes<Visitante> {

	@EJB
	private IVisitanteDAO visitanteDAO;

	@Override
	public List<Visitante> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		return null;
	}

	@Override
	public int total(Long idCondominio) {
		return 0;
	}

	@Override
	public List<Visitante> recuperarPorApartamentoPaginado(Long idApartamento, int inicio, int qtdRegistros) {
		return visitanteDAO.recuperarPaginadoApartamento(idApartamento, inicio, qtdRegistros);
	}

	@Override
	public int totalVisitantesApto(Long idApartamento) {
		return visitanteDAO.totalVisitantesApartamento(idApartamento);
	}


}

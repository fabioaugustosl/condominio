package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Visitante;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IVisitanteService extends PaginacaoService<Visitante> {
	public List<Visitante> recuperar(Long idCondominio);
	public void salvarVisitante(Visitante visitante) throws AppException ;
	public void saidaVisitante(Long idVisitante) throws AppException ;
	public void remover(Long idVisitante) throws AppException;
	
	public int totalVisitantes(Long idCondominio);
	public List<Visitante> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros);
}



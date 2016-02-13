package br.com.virtz.condominio.service;

import java.util.List;

import br.com.virtz.condominio.entidades.Recebido;

public interface PaginacaoService<T> {

	public List<T> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros); 
	public int totalVisitantes(Long idCondominio);
	
	// paginado por apto
	public List<Recebido> recuperarPorApartamentoPaginado(Long idApartamento, int inicio, int qtdRegistros); 
	public int totalVisitantesApto(Long idApartamento);
	
}

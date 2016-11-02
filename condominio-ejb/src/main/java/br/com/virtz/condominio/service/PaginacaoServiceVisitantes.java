package br.com.virtz.condominio.service;

import java.util.List;

import br.com.virtz.condominio.entidades.Recebido;

public interface PaginacaoServiceVisitantes<T> extends PaginacaoService<T> {

	// paginado por apto
	public List<Recebido> recuperarPorApartamentoPaginado(Long idApartamento, int inicio, int qtdRegistros);
	public int totalVisitantesApto(Long idApartamento);

}

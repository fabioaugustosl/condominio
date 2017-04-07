package br.com.virtz.condominio.service;

import java.util.List;

import br.com.virtz.condominio.entidades.Recebido;
import br.com.virtz.condominio.entidades.Visitante;

public interface PaginacaoServiceVisitantes<T> extends PaginacaoService<T> {

	// paginado por apto
	public List<Visitante> recuperarPorApartamentoPaginado(Long idApartamento, int inicio, int qtdRegistros);
	public int totalVisitantesApto(Long idApartamento);

}

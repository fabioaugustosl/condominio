package br.com.virtz.condominio.service;

import java.util.List;

public interface PaginacaoService<T> {
	public List<T> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros);
	public int total(Long idCondominio);
}

package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Estado;

@Local
public interface ILocalidadeService {
	public List<Estado> recuperarTodosEstados();
	public List<Cidade> recuperarPorEstado(Long idEstado);
}

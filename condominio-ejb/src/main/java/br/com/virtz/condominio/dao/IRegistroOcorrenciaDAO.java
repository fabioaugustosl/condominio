package br.com.virtz.condominio.dao;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.RegistroOcorrencia;

@Local
public interface IRegistroOcorrenciaDAO extends CrudDAO<RegistroOcorrencia> {
	public List<RegistroOcorrencia> recuperar(Long idCondominio);
	public List<RegistroOcorrencia> recuperarPaginado(Long idCondominio, int inicio, int qtdRegistros);
	public int totalOcorrencias(Long idCondominio);
}

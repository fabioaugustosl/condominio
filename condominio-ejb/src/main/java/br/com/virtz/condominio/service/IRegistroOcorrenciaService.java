package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.RegistroOcorrencia;

@Local
public interface IRegistroOcorrenciaService extends PaginacaoService<RegistroOcorrencia> {
	public RegistroOcorrencia salvar(RegistroOcorrencia registro) throws Exception;
	public void remover(Long id);
	public RegistroOcorrencia recuperarRegistroOcorrencia(Long id);
	public List<RegistroOcorrencia> recuperarTodos(Long idCondominio);
	public List<RegistroOcorrencia> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros);
}

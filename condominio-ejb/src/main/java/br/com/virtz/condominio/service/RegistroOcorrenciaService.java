package br.com.virtz.condominio.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IRegistroOcorrenciaDAO;
import br.com.virtz.condominio.entidades.RegistroOcorrencia;

@Stateless
public class RegistroOcorrenciaService implements IRegistroOcorrenciaService {

	@EJB
	private IRegistroOcorrenciaDAO registroDAO;


	@Override
	public RegistroOcorrencia salvar(RegistroOcorrencia registro)
			throws Exception {
		registro.setDataRegistro(new Date());
		return this.registroDAO.salvar(registro);
	}

	@Override
	public RegistroOcorrencia recuperarRegistroOcorrencia(Long id) {
		return this.registroDAO.recuperarPorId(id);
	}

	@Override
	public void remover(Long id) {
		this.registroDAO.remover(id);
	}

	@Override
	public List<RegistroOcorrencia> recuperarTodos(Long idCondominio) {
		return this.registroDAO.recuperar(idCondominio);
	}

	@Override
	public List<RegistroOcorrencia> recuperarPorCondominioPaginado(Long idCondominio, int inicio, int qtdRegistros) {
		return registroDAO.recuperarPaginado(idCondominio, inicio, qtdRegistros);
	}

	@Override
	public int total(Long idCondominio) {
		return registroDAO.totalOcorrencias(idCondominio);
	}

}

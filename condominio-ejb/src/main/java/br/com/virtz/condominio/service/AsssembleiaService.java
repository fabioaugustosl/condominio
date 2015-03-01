package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IAssembleiaDAO;
import br.com.virtz.condominio.dao.IPautaDAO;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.exception.ErroAoSalvar;

@Stateless
public class AsssembleiaService implements IAssembleiaService {

	@EJB
	private IAssembleiaDAO assembleiaDAO;
	
	@EJB
	private IPautaDAO pautaDAO;
	
	
	

	@Override
	public Assembleia salvar(Assembleia assembleia) throws ErroAoSalvar {
		try {
			return assembleiaDAO.salvar(assembleia);
		} catch (Exception e) {
			throw new ErroAoSalvar("Ocorreu um erro ao salvar a assembléia.", assembleia);
		}
	}

	@Override
	public List<Assembleia> recuperarTodos(Long idCondominio) {
		return assembleiaDAO.recuperar(idCondominio);
	}

	@Override
	public void removerPauta(Long idPauta) {
		pautaDAO.remover(idPauta);		
	}

	@Override
	public void removerAssembleia(Long id) {
		assembleiaDAO.remover(id);
	}

	@Override
	public Assembleia recuperar(Long idAssembleia) {
		return assembleiaDAO.recuperarPorId(idAssembleia);
	}
	

}

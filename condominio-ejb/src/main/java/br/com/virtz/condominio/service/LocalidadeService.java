package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.EstadoDAO;
import br.com.virtz.condominio.dao.ICidadeDAO;
import br.com.virtz.condominio.dao.IEstadoDAO;
import br.com.virtz.condominio.entidades.Cidade;
import br.com.virtz.condominio.entidades.Estado;

@Stateless
public class LocalidadeService implements ILocalidadeService {

	@EJB
	private IEstadoDAO estadoDAO;
	
	@EJB
	private ICidadeDAO cidadeDAO;

	@Override
	public List<Cidade> recuperarPorEstado(Long idEstado) {
		return cidadeDAO.recuperarPorEstado(idEstado);
	}

	@Override
	public List<Estado> recuperarTodosEstados() {
		List<Estado> estados = estadoDAO.recuperarTodos();
		return estados;
	}
	
	

}

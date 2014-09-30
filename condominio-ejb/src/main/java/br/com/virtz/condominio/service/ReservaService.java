package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IReservaDAO;
import br.com.virtz.condominio.entity.Reserva;

@Stateless
public class ReservaService implements IReservaService {

	@EJB
	private IReservaDAO reservaDAO;

	@Override
	public Reserva salvar(Reserva reserva) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remover(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Reserva> recuperarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	
}

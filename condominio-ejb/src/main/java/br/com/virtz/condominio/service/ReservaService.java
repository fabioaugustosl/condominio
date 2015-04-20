package br.com.virtz.condominio.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.condominio.dao.IReservaDAO;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;

@Stateless
public class ReservaService implements IReservaService {

	@EJB
	private IReservaDAO reservaDAO;

	@Override
	public Reserva salvar(Reserva reserva) throws Exception {
		return reservaDAO.salvar(reserva);
	}

	@Override
	public void remover(Long id) {
		reservaDAO.remover(id);
	}

	@Override
	public List<Reserva> recuperarTodos() {
		return reservaDAO.recuperarTodos();
	}

	@Override
	public List<Reserva> recuperar(AreaComum area) {
		return reservaDAO.recuperar(area);
	}
	
	@Override
	public void remover(AreaComum areaReservada, String nomeUsuarioReserva, Date dataInicioReserva){
		
	}
	
}

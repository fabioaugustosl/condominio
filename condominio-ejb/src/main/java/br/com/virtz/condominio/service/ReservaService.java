package br.com.virtz.condominio.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import freemarker.template.utility.DateUtil;
import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.dao.IReservaDAO;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class ReservaService implements IReservaService {

	@EJB
	private IReservaDAO reservaDAO;

	@Override
	public Reserva salvar(Reserva reserva) throws Exception {
		DataUtil dtUtil = new DataUtil();
		Calendar d = Calendar.getInstance();
		Date data = dtUtil.limparHora(reserva.getData().getTime());
		d.setTime(data);
		reserva.setData(d);
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
	public void remover(AreaComum areaReservada, String emailUsuarioReserva, Date dataInicioReserva) throws AppException{
		Reserva r = reservaDAO.recuperarPorAreaEmailEData(areaReservada, emailUsuarioReserva, dataInicioReserva);
		if (r == null){
			throw new AppException("Ocorreu um erro ao recuperar a reserva para excluí-la.");
		}
		reservaDAO.remover(r.getId());
	}
	
}

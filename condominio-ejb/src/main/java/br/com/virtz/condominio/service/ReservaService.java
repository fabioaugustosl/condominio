package br.com.virtz.condominio.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.dao.IApartamentoDAO;
import br.com.virtz.condominio.dao.IReservaDAO;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.exception.AppException;

@Stateless
public class ReservaService implements IReservaService {

	@EJB
	private IReservaDAO reservaDAO;

	@EJB
	private IApartamentoDAO aptoDAO;

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
		DataUtil util = new DataUtil();

		Reserva r = reservaDAO.recuperarPorAreaEmailEData(areaReservada, emailUsuarioReserva, dataInicioReserva);
		if (r == null){

			List<Reserva> reservasUsuario = reservaDAO.recuperarPorAreaEEmail(areaReservada, emailUsuarioReserva);
			if(reservasUsuario!=null && !reservasUsuario.isEmpty()){
				for(Reserva res : reservasUsuario){
					if(util.mesmoDiaMesAno(dataInicioReserva, res.getData().getTime())){
						r = res;
						break;
					}
				}
			}

			if (r == null){
				throw new AppException("Ocorreu um erro ao recuperar a reserva para excluí-la.");
			}
		}
		reservaDAO.remover(r.getId());
	}

	@Override
	public void removerProAptoEData(AreaComum areaReservada, String apto, String bloco, Date dataInicioReserva) throws AppException {
		if(apto == null || areaReservada == null || bloco == null || dataInicioReserva == null){
			throw new AppException("Todos os campos são obrigatórios");
		}

		Apartamento ap = aptoDAO.recuperarPorNumero(areaReservada.getCondominio().getId(), apto, bloco);
		if(ap == null){
			throw new AppException("Não foi possível recuperar o apartamento da reserva para remoção. Favor entrar em contato com o suporte técnico.");
		}

		DataUtil util = new DataUtil();

		Reserva r = reservaDAO.recuperarPorAreaAptoEData(areaReservada, ap.getId(), dataInicioReserva);
		if (r == null){

			List<Reserva> reservasUsuario = reservaDAO.recuperarPorAreaEApto(areaReservada,  ap.getId());
			if(reservasUsuario!=null && !reservasUsuario.isEmpty()){
				for(Reserva res : reservasUsuario){
					if(util.mesmoDiaMesAno(dataInicioReserva, res.getData().getTime())){
						r = res;
						break;
					}
				}
			}

			if (r == null){
				throw new AppException("Ocorreu um erro ao recuperar a reserva para excluí-la.");
			}
		}
		reservaDAO.remover(r.getId());
	}



	@Override
	public void removerProAptoEData(AreaComum areaReservada, String apto, String bloco, String agrupamento, Date dataInicioReserva) throws AppException {
		if(apto == null || areaReservada == null || bloco == null  || agrupamento == null  || dataInicioReserva == null){
			throw new AppException("Todos os campos são obrigatórios");
		}

		Apartamento ap = aptoDAO.recuperarPorNumero(areaReservada.getCondominio().getId(), apto, bloco,agrupamento);
		if(ap == null){
			throw new AppException("Não foi possível recuperar o apartamento da reserva para remoção. Favor entrar em contato com o suporte técnico.");
		}

		DataUtil util = new DataUtil();

		Reserva r = reservaDAO.recuperarPorAreaAptoEData(areaReservada, ap.getId(), dataInicioReserva);
		if (r == null){

			List<Reserva> reservasUsuario = reservaDAO.recuperarPorAreaEApto(areaReservada,  ap.getId());
			if(reservasUsuario!=null && !reservasUsuario.isEmpty()){
				for(Reserva res : reservasUsuario){
					if(util.mesmoDiaMesAno(dataInicioReserva, res.getData().getTime())){
						r = res;
						break;
					}
				}
			}

			if (r == null){
				throw new AppException("Ocorreu um erro ao recuperar a reserva para excluí-la.");
			}
		}
		reservaDAO.remover(r.getId());
	}

	@Override
	public List<Reserva> recuperarRecentes(AreaComum area) {
		Integer ano = Calendar.getInstance().get(Calendar.YEAR);
		Integer mes = Calendar.getInstance().get(Calendar.MONTH);
		List<Reserva> reservas = reservaDAO.recuperarReservarAPartir(area, ano, mes);

		if(mes >= 10){
			if(reservas == null){
				reservas = new ArrayList<Reserva>();
			}
			List<Reserva> reservasProximoAno = reservaDAO.recuperarReservarAPartir(area, (ano+1), 0);
			if(reservasProximoAno != null && !reservasProximoAno.isEmpty()){
				reservas.addAll(reservasProximoAno);
			}
		}

		return reservas;
	}

}

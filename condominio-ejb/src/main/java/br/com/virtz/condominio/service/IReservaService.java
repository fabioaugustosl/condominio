package br.com.virtz.condominio.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IReservaService {
	public Reserva salvar(Reserva reserva) throws Exception;
	public void remover(Long id);
	public List<Reserva> recuperarTodos();
	public List<Reserva> recuperar(AreaComum area);
	public List<Reserva> recuperarRecentes(AreaComum area);
	public void remover(AreaComum areaReservada, String nomeUsuarioReserva, Date dataInicioReserva) throws AppException;
	public void removerProAptoEData(AreaComum areaReservada, String apto, String bloco, Date dataInicioReserva) throws AppException;
}

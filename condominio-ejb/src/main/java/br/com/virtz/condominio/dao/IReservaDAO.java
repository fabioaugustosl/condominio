package br.com.virtz.condominio.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;

@Local
public interface IReservaDAO extends CrudDAO<Reserva> {
	public List<Reserva> recuperar(AreaComum area);
	public List<Reserva> recuperarReservarAPartir(AreaComum area, Integer ano, Integer mes);
	public Reserva recuperar(AreaComum area, String nomeUsuario, Date dataInicioReserva);
	public Reserva recuperarPorAreaEmailEData(AreaComum area, String emailUsuario, Date dataInicioReserva) ;
	public Reserva recuperarPorAreaAptoEData(AreaComum area, Long idApto, Date dataInicioReserva) ;
	public List<Reserva> recuperarPorAreaEEmail(AreaComum area, String emailUsuario);
	public List<Reserva> recuperarPorAreaEApto(AreaComum area, Long idApartamento);
	public List<Reserva> recuperarPorCondominio(Long idCondominio);
	
}

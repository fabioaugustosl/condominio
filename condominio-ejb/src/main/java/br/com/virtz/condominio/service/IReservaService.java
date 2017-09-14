package br.com.virtz.condominio.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IReservaService {
	public Reserva salvar(Reserva reserva) throws Exception;
	public void remover(Long id);
	public List<Reserva> recuperarTodos();
	public List<Reserva> recuperar(AreaComum area);
	public List<Reserva> recuperarRecentes(AreaComum area);
	public List<Reserva> recuperarPorCondominio(Long idCondominio);
	public void remover(AreaComum areaReservada, String nomeUsuarioReserva, Date dataInicioReserva) throws AppException;
	public void removerProAptoEData(AreaComum areaReservada, String apto, String bloco, Date dataInicioReserva) throws AppException;
	public void removerProAptoEData(AreaComum areaReservada, String apto, String bloco, String agrupamento, Date dataInicioReserva) throws AppException;
	public List<Reserva> recuperarReservarRecentesPorCondominio(Integer idCondominio);
	public void validarReserva(Calendar data, AreaComum areaSelecionada, boolean liEConcordo) throws Exception;
	public void verificarUsuarioBloqueado(Usuario usu) throws Exception;
	public boolean verificarSePodeRemoverReserva(Usuario usuarioLogado, String numeroAptoReserva, String nomeBlocoReserva);
}

package br.com.virtz.condominio.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.dao.IApartamentoDAO;
import br.com.virtz.condominio.dao.IBloqueioFuncaoUsuarioDAO;
import br.com.virtz.condominio.dao.IReservaDAO;
import br.com.virtz.condominio.dao.IUsuarioDAO;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;

@Stateless
public class ReservaService implements IReservaService {

	@EJB
	private IReservaDAO reservaDAO;

	@EJB
	private IUsuarioDAO usuarioDAO;

	@EJB
	private IBloqueioFuncaoUsuarioDAO bloqueioDAO;

	@EJB
	private IApartamentoDAO aptoDAO;

	@Inject
	private ParametroSistemaLookup parametroLookup;

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

	public List<Reserva> recuperarReservarRecentesPorCondominio(Integer idCondominio){
		Integer ano = Calendar.getInstance().get(Calendar.YEAR);
		Integer mes = Calendar.getInstance().get(Calendar.MONTH);
		List<Reserva> reservas = reservaDAO.recuperarReservarPorCondominioAPartir(idCondominio, ano, mes);

		if(mes >= 10){
			if(reservas == null){
				reservas = new ArrayList<Reserva>();
			}
			List<Reserva> reservasProximoAno = reservaDAO.recuperarReservarPorCondominioAPartir(idCondominio, (ano+1), 0);
			if(reservasProximoAno != null && !reservasProximoAno.isEmpty()){
				reservas.addAll(reservasProximoAno);
			}
		}

		return reservas;
	}

	@Override
	public List<Reserva> recuperarPorCondominio(Long idCondominio) {
		return reservaDAO.recuperarPorCondominio(idCondominio);
	}


	public void validarReserva(Calendar data, AreaComum areaSelecionada, boolean liEConcordo) throws Exception{

		ParametroSistema maximoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM);
		ParametroSistema minimoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MINIMO_PARA_AGENDAR_AREA_COMUM);

		DataUtil dt = new DataUtil();
		// não pode marcar evento retroativo
		if(dt.dataEhMaiorQueHoje(data.getTime())){
			throw new Exception("Não é permitido marcar eventos retroativos.");
		}

		// caso tenha sido cadastrada alguma instrução para reserva o usuário tem q concordar com o termo.
		if(StringUtils.isNotBlank(areaSelecionada.getInstrucoesReserva()) && !liEConcordo){
			throw new Exception("Para efetuar a reservar você deve ler e concordar com o termo/instrução.");
		}

		// se data acima do limite deve rolar uma exceção
		Date dataMaxima = getDataMaximaAgendamento(maximoDias);
		if(dataMaxima != null && data.getTime().after(dataMaxima)){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			throw new Exception("A reserva não foi realizada. A data limite para agendamentos é "+sdf.format(dataMaxima)+". ");
		}

		// valida a proximidade da data da reserva. Se tiver configurado uma procedencia minima a reserva deve ser barrada.
		if(minimoDias != null && Integer.valueOf(minimoDias.getValor()) > 0){
			if(getDiferencaDiasEntreHojeEaReserva(data.getTime()) < Integer.valueOf(minimoDias.getValor())){
				throw new Exception("A reserva não foi realizada. A quantidade mínima de antecedência para reserva é de "+minimoDias.getValor()+" dias. ");
			}
		}

	}


	public boolean verificarSePodeRemoverReserva(Usuario usuarioLogado, String numeroAptoReserva, String nomeBlocoReserva) {
		if(usuarioLogado.isAdministrativo() || usuarioLogado.isSindico()){
			return Boolean.TRUE;
		}

		if(usuarioLogado.getApartamento().getNumero().equals(numeroAptoReserva)
				&& usuarioLogado.getApartamento().getBloco().getNome().equals(nomeBlocoReserva)){
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	public void validarUsuarioBloqueadoReserva(Apartamento aptoAgendamento) throws Exception{
		List<Usuario> usuarios = usuarioDAO.recuperarUsuariosPorApartamento(aptoAgendamento.getId());
		if(usuarios != null && !usuarios.isEmpty()){
			for(Usuario u : usuarios){
				verificarUsuarioBloqueado(u);
			}
		}
	}

	public void verificarUsuarioBloqueado(Usuario usu) throws Exception {
		List<BloqueioFuncaoUsuario> bloqueioUsuario =  bloqueioDAO.recuperarPorUsuarioEFuncao(usu.getId(), EnumFuncaoBloqueio.RESERVA);
		if(bloqueioUsuario != null && !bloqueioUsuario.isEmpty()){
			String comentario = bloqueioUsuario.get(0).getComentario();
			if(StringUtils.isBlank(comentario)){
				comentario = EnumFuncaoBloqueio.RESERVA.getMsgParaUsuario();
			}
			throw new Exception(comentario);
		}
	}


	private Date getDataMaximaAgendamento(ParametroSistema maximoDias){
		if(maximoDias != null && maximoDias.getValor() !=null ){
			DataUtil dt =new DataUtil();
			return dt.adicionarDias(new Date(), Integer.parseInt(maximoDias.getValor()));
		}
		return null;
	}

	private Integer getDiferencaDiasEntreHojeEaReserva(Date dataReserva){
		DataUtil dt =new DataUtil();
		return dt.diasEntreDatas(dt.agora().getTime(), dt.limparHora(dataReserva));
	}


}

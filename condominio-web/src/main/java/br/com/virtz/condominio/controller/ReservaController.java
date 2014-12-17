package br.com.virtz.condominio.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ReservaController {
	
	@EJB
	private IReservaService reservaService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	private ScheduleModel reservas;
	private ScheduleEvent evento = new DefaultScheduleEvent();
	
	private Set<AreaComum> areas;
	private Usuario usuario;
	private AreaComum areaSelecionada = null;
	private String mensagemConfirmacaoReserva = null ;
	private ParametroSistema maximoDias = null;
	
	@PostConstruct
	public void init(){
		reservas = new DefaultScheduleModel();
		usuario = sessao.getUsuarioLogado();
		areas = usuario.getCondominio().getAreasComuns();
		maximoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM);
	}


	public void recuperarEventos() {
		reservas.clear();
		if(getAreaSelecionada() != null){
			List<Reserva> reservasPersistidas = reservaService.recuperar(getAreaSelecionada());
			for(Reserva r : reservasPersistidas){
				reservas.addEvent(new DefaultScheduleEvent(r.getUsuario().getNome(), r.getData().getTime(), r.getData().getTime()));
			}
		}
	}

     
    public void onDateSelect(SelectEvent selectEvent) throws AppException {
    	if(getAreaSelecionada() == null){
    		throw new AppException("Favor selecionar uma área para efetuar a reserva.");
    	}
    	
    	Date dataSelecionada = (Date) selectEvent.getObject();
    	
    	// validar se existe uma reserva para área nessa data.
    	for(ScheduleEvent event :reservas.getEvents()){
    		if(event.getStartDate().equals(dataSelecionada)){
    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    			throw new AppException("Já existe uma reserva cadastrada para o(a) "+getAreaSelecionada().getNome()+" no dia "+sdf.format(dataSelecionada));
    		}
    	}
    	
    	String nomeUsuario = "";
    	if(usuario != null){
    		nomeUsuario = usuario.getNome();
    	}
        evento = new DefaultScheduleEvent(nomeUsuario, dataSelecionada, dataSelecionada);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String txtArea = " para o(a) "+getAreaSelecionada().getNome();
		mensagemConfirmacaoReserva = "Você confirma a reserva"+txtArea+" para o dia "+sdf.format(evento.getStartDate())+"?";
    }
     
   
	public void salvarReserva(ActionEvent actionEvent) throws AppException {
        if(evento.getId() == null) {
        	reservas.addEvent(evento);
        } else {
        	reservas.updateEvent(evento);
        }
        
        salvar();
        
        evento = new DefaultScheduleEvent();
        this.mensagemConfirmacaoReserva = "";
        
        message.addInfo("Sua reserva foi confirmada com sucesso!");
    }


	private void salvar() throws AppException {
		Calendar data = Calendar.getInstance();
		data.setTime(evento.getStartDate());

		// se data acima do limite deve rolar uma exceção
		Date dataMaxima = getDataMaximaAgendamento();
		if(dataMaxima != null && data.after(getDataMaximaAgendamento())){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			throw new AppException("A reserva não foi realizada. A data limite para agendamentos é "+sdf.format(dataMaxima)+". ");
		}
		
		Reserva reserva = new Reserva();
        reserva.setAreaComum(getAreaSelecionada());
        reserva.setData(data);
        reserva.setUsuario(this.usuario);
        
        try {
			reservaService.salvar(reserva);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a reserva.");
		}
	}
	
	
	/**
	 * Retorna a data máxima permitida para agendamento.
	 * É considerado o parametro de sistemas id = 2;
	 * @return
	 */
	public Date getDataMaximaAgendamento(){
		if(maximoDias != null){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, getMaximoDiasFuturo());
			return c.getTime();
		}
		return null;
	}
	
	
	
	/*  GETTERS e SETTERs	 */
	public String getMensagemConfirmacaoReserva(){
		return mensagemConfirmacaoReserva;
	}
	
	public Set<AreaComum> getAreas() {
		return areas;
	}

	public ScheduleModel getReservas() {
		return reservas;
	}

	public void setReservas(ScheduleModel reservas) {
		this.reservas = reservas;
	}

	public AreaComum getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(AreaComum areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}
	
	public Integer getMaximoDiasFuturo(){
		if(maximoDias != null){
			return Integer.parseInt(maximoDias.getValor());
		}
		return 0;
	}

	
}

package br.com.virtz.condominio.controller;

import java.text.SimpleDateFormat;
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

import br.com.virtz.condominio.entity.AreaComum;
import br.com.virtz.condominio.entity.Reserva;
import br.com.virtz.condominio.entity.Usuario;
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
	private MessageHelper message;
	
	private ScheduleModel reservas;
	private ScheduleEvent evento = new DefaultScheduleEvent();
	
	private Set<AreaComum> areas;
	private Usuario usuario;
	private AreaComum areaSelecionada = null;
	private String mensagemConfirmacaoReserva = null ;
	private String txtArea = null;
	
	
	@PostConstruct
	public void init(){
		
		reservas = new DefaultScheduleModel();

		usuario = sessao.getUsuarioLogado();
		areas = usuario.getCondominio().getAreasComuns();
		
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

     
    public void onDateSelect(SelectEvent selectEvent) {
    	String nomeUsuario = "";
    	if(usuario != null){
    		nomeUsuario = usuario.getNome();
    	}
        evento = new DefaultScheduleEvent(nomeUsuario, (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(getAreaSelecionada() != null){
			txtArea =" para o(a) "+getAreaSelecionada().getNome();
		} else {
			txtArea ="";
		}
		mensagemConfirmacaoReserva = "Você confirma a reserva"+txtArea+" para o dia "+sdf.format(evento.getStartDate())+"?";
    }
     
   
	public void salvarReserva(ActionEvent actionEvent) {
        if(evento.getId() == null) {
        	reservas.addEvent(evento);
        } else {
        	reservas.updateEvent(evento);
        }
         
        evento = new DefaultScheduleEvent();
        mensagemConfirmacaoReserva = "";
        
        message.addInfo("Sua reserva foi confirmada com sucesso!");
        
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
		if(areaSelecionada != null){
			txtArea = areaSelecionada.getNome();
		}
		this.areaSelecionada = areaSelecionada;
	}

	
}

package br.com.virtz.condominio.controller;

import java.util.Date;
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
	
	
	@PostConstruct
	public void init(){
		
		reservas = new DefaultScheduleModel();

		usuario = sessao.getUsuarioLogado();
		areas = usuario.getCondominio().getAreasComuns();
		
//		recuperarEventos(usuario.getCondominio());
		
	}


	private void recuperarEventos(AreaComum area) {
		// para criar os eventos
//		reservas.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
//        reservas.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
	}

	
	public void selecionarData(SelectEvent selectEvent) {
        evento = new DefaultScheduleEvent(usuario.getNome(), (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
	
	public void salvarReserva(ActionEvent actionEvent) {
        if(evento.getId() == null)
        	reservas.addEvent(evento);
        else
        	reservas.updateEvent(evento);
         
        evento = new DefaultScheduleEvent();
    }
	
	
	
	/*  GETTERS e SETTERs	 */
	
	public Set<AreaComum> getAreas() {
		return areas;
	}

	public ScheduleModel getReservas() {
		return reservas;
	}

	public void setReservas(ScheduleModel reservas) {
		this.reservas = reservas;
	}
	
		
}

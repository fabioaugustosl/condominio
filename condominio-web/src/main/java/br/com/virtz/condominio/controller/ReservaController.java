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
import br.com.virtz.condominio.geral.DataUtil;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ReservaController {
	
	@EJB
	private IReservaService reservaService;
	
	@EJB
	private IUsuarioService usuarioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private PrincipalController principalController;
	
	private ScheduleModel reservas;
	private ScheduleEvent evento = new DefaultScheduleEvent();
	
	private Set<AreaComum> areas;
	private List<Usuario> usuarios;
	private Usuario usuario;
	private AreaComum areaSelecionada = null;
	private String mensagemConfirmacaoReserva = null ;
	private ParametroSistema maximoDias = null;
	
	private boolean podeRemoverReserva;
	private String mensagemExclusaoReserva = null;
	
	
	@PostConstruct
	public void init(){
		reservas = new DefaultScheduleModel();
		usuario = sessao.getUsuarioLogado();
		areas = usuario.getCondominio().getAreasComuns();
		maximoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM);
		areaSelecionada = null;
		podeRemoverReserva = false;
		mensagemExclusaoReserva = null;
		
		// se for sindico pode agendar para outros moradores
		if(principalController.ehSindico() || principalController.ehAdministrativo()){
			usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		} else {
			usuarios = null;
		}
	}


	public void recuperarEventos() {
		reservas.clear();
		if(getAreaSelecionada() != null){
			List<Reserva> reservasPersistidas = reservaService.recuperar(getAreaSelecionada());
			for(Reserva r : reservasPersistidas){
				reservas.addEvent(new DefaultScheduleEvent(montarNomeEvento(usuario), r.getData().getTime(), r.getData().getTime()));
			}
		}
	}

	
	public void onEventSelect(SelectEvent selectEvent) {
        evento = (ScheduleEvent) selectEvent.getObject();
        podeRemoverReserva = verificarSePodeRemoverReserva();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mensagemExclusaoReserva = "Tem certeza que você deseja remover a reserva do(a)"+getAreaSelecionada().getNome()+" para o dia "+sdf.format(evento.getStartDate())+"?";
    }
    
	
    public void onDateSelect(SelectEvent selectEvent) throws AppException {
    	if(getAreaSelecionada() == null){
    		return;
    		//throw new AppException("Favor selecionar uma área para efetuar a reserva.");
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
        evento = new DefaultScheduleEvent(montarNomeEvento(usuario), dataSelecionada, dataSelecionada);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String txtArea = " para o(a) "+getAreaSelecionada().getNome();
		mensagemConfirmacaoReserva = "Você confirma a reserva"+txtArea+" para o dia "+sdf.format(evento.getStartDate())+"?";
    }


	private String montarNomeEvento(Usuario usuario) {
		return usuario.getNome()+" ["+usuario.getEmail()+"]";
	}
     

	public void salvarReserva() throws AppException {
		salvar();

		if(evento.getId() == null) {
			reservas.addEvent(evento);
		} else {
			reservas.updateEvent(evento);
		}
		
        evento = new DefaultScheduleEvent();
        this.mensagemConfirmacaoReserva = "";
        
        message.addInfo("Sua reserva foi confirmada com sucesso!");
    }
	
	public void mensagemSucesso(){
		message.addInfo("Sua reserva foi confirmada com sucesso!");
	}


	private void salvar() throws AppException {
		Calendar data = Calendar.getInstance();
		data.setTime(evento.getStartDate());

		DataUtil dt = new DataUtil();
		// não pode marcar evento retroativo
		if(dt.dataEhMaiorQueHoje(data.getTime())){
			throw new AppException("Não é permitido marcar eventos retroativos.");
		}
		
		// se data acima do limite deve rolar uma exceção
		Date dataMaxima = getDataMaximaAgendamento();
		if(dataMaxima != null && data.getTime().after(dataMaxima)){
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
	
	public void removerReserva() throws AppException {
        if(evento != null) {
        	String email = recuperarEmailDaReserva();
        	reservaService.remover(getAreaSelecionada(), email, evento.getStartDate());
        	reservas.deleteEvent(evento);
        	message.addInfo("A reserva foi removida com sucesso!");
        }
    }
	
	
	/**
	 * Retorna a data máxima permitida para agendamento.
	 * É considerado o parametro de sistemas id = 2;
	 * @return
	 */
	public Date getDataMaximaAgendamento(){
		if(maximoDias != null && maximoDias.getValor() !=null ){
			DataUtil dt =new DataUtil();
			return dt.adicionarDias(new Date(), Integer.parseInt(maximoDias.getValor()));
		}
		return null;
	}
	
	
	public boolean verificarSePodeRemoverReserva() {
		if(usuario.isAdministrativo() || usuario.isSindico()){
			return Boolean.TRUE;
		}
		
		String email = recuperarEmailDaReserva();
		if(usuario.getEmail().equals(email)){
			return Boolean.TRUE;
		}
				
		return Boolean.FALSE;
	}


	private String recuperarEmailDaReserva() {
		String  emailReserva = evento.getTitle().substring(evento.getTitle().indexOf("["));
		emailReserva = emailReserva.substring(1, emailReserva.indexOf("]"));
		return emailReserva;
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

	public boolean isPodeRemoverReserva() {
		return podeRemoverReserva;
	}

	public void setPodeRemoverReserva(boolean podeRemoverReserva) {
		this.podeRemoverReserva = podeRemoverReserva;
	}

	public String getMensagemExclusaoReserva() {
		return mensagemExclusaoReserva;
	}

	public void setMensagemExclusaoReserva(String mensagemExclusaoReserva) {
		this.mensagemExclusaoReserva = mensagemExclusaoReserva;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}

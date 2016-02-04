package br.com.virtz.condominio.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
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
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@Inject
	private PrincipalController principalController;
	
	private ScheduleModel reservas;
	private ScheduleEvent evento = new DefaultScheduleEvent();
	
	private Set<AreaComum> areas;
	private List<Usuario> usuarios;
	private Usuario usuario = null;
	private Usuario usuarioLogado = null;
	private AreaComum areaSelecionada = null;
	private String mensagemConfirmacaoReserva = null ;
	private String mensagemErroReserva = null ;
	private ParametroSistema maximoDias = null;
	
	private boolean podeRemoverReserva;
	private String mensagemExclusaoReserva = null;
	
	
	@PostConstruct
	public void init(){
		reservas = new DefaultScheduleModel();
		usuarioLogado = sessao.getUsuarioLogado();

		montarListaAreasParaReserva();
		
		maximoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM);
		areaSelecionada = null;
		podeRemoverReserva = false;
		mensagemExclusaoReserva = null;
		
		// se for sindico pode agendar para outros moradores
		if(principalController.ehSindico() || principalController.ehAdministrativo()){
			usuarios = usuarioService.recuperarTodos(usuarioLogado.getCondominio());
		} else {
			usuarios = null;
		}
	}


	private void montarListaAreasParaReserva() {
		areas = new HashSet<AreaComum>();
		for(AreaComum a : usuarioLogado.getCondominio().getAreasComuns()){
			if(a.isPodeSerReservado()){
				areas.add(a);
			}
		}
	}


	public void recuperarEventos() {
		reservas.clear();
		Usuario usu = usuario == null ? usuarioLogado : usuario;
		if(getAreaSelecionada() != null){
			List<Reserva> reservasPersistidas = reservaService.recuperar(getAreaSelecionada());
			for(Reserva r : reservasPersistidas){
				Calendar hora = r.getData();
				hora.add(Calendar.HOUR_OF_DAY, 12);
				//hora.add(Calendar.DATE, 1); // gambiarra pra burlar o pau do componente q sempre exibe a data -1 dia.
				reservas.addEvent(new DefaultScheduleEvent(montarNomeEvento(r.getUsuario()), hora.getTime(), hora.getTime()));
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
    	for(ScheduleEvent event : reservas.getEvents()){
    		if(event.getStartDate().equals(dataSelecionada)){
    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    			mensagemErroReserva = "Já existe uma reserva cadastrada para o(a) "+getAreaSelecionada().getNome()+" no dia "+sdf.format(dataSelecionada)+".";
    			mensagemConfirmacaoReserva =null;
    			return;
    		}
    	}
    	
    	Usuario usu = (usuario == null) ? usuarioLogado : usuario;
    	
        evento = new DefaultScheduleEvent(montarNomeEvento(usu), dataSelecionada, dataSelecionada);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String txtArea = " para o(a) "+getAreaSelecionada().getNome();
		mensagemConfirmacaoReserva = "Você confirma a reserva"+txtArea+" para o dia "+sdf.format(evento.getStartDate())+"?";
		mensagemErroReserva = null;
    }


	private String montarNomeEvento(Usuario usuario) {
		return usuario.getNomeExibicao()+" ["+usuario.getEmail()+"]";
	}
     

	public void salvarReserva() throws AppException {
		salvar();

		if(evento.getId() == null) {
			reservas.addEvent(evento);
		} else {
			reservas.updateEvent(evento);
		}
		
        evento = new DefaultScheduleEvent();
        this.mensagemConfirmacaoReserva = null;
        mensagemErroReserva = null;
        
        usuario = null;
        
        message.addInfo("Sua reserva foi confirmada com sucesso!");
    }
	
	
	private void enviarEmailConfirmacaoReserva(Usuario usuario, Reserva reserva) {
		DataUtil dataUtil = new DataUtil();
		
		Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
		mapParametrosEmail.put("nome_usuario", usuario.getNomeExibicao());
		mapParametrosEmail.put("data_reserva", dataUtil.formatarData(reserva.getData().getTime(),"dd/MM/yyyy"));
		mapParametrosEmail.put("nome_area", reserva.getAreaComum().getNome());
		
		String msg = leitor.processarTemplate( arquivoUtil.getCaminhaPastaTemplatesEmail(), EnumTemplates.CONFIRMACAO_RESERVA_AREA.getNomeArquivo(), mapParametrosEmail);
		
		Email email = new Email(EnumTemplates.PAUTA_ENVIADA.getDe(), usuario.getEmail(), EnumTemplates.CONFIRMACAO_RESERVA_AREA.getAssunto(), msg);
		enviarEmail.enviar(email);
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
		
		Usuario usu = (usuario == null) ? usuarioLogado : usuario;
		
		Reserva reserva = new Reserva();
        reserva.setAreaComum(getAreaSelecionada());
        reserva.setData(data);
        reserva.setUsuario(usu);
        
        try {
			reservaService.salvar(reserva);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a reserva.");
		}
        try {
        	  enviarEmailConfirmacaoReserva(usu, reserva);
		} catch (Exception e) {
			
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
		if(usuarioLogado.isAdministrativo() || usuarioLogado.isSindico()){
			return Boolean.TRUE;
		}
		
		Usuario usu = (usuario == null) ? usuarioLogado : usuario;
		String email = recuperarEmailDaReserva();
		if(usu.getEmail().equals(email)){
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

	public String getMensagemErroReserva() {
		return mensagemErroReserva;
	}
	
	
}

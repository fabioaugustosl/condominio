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

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumFuncaoBloqueio;
import br.com.virtz.condominio.constantes.EnumParametroSistema;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.AgrupamentoUnidades;
import br.com.virtz.condominio.entidades.Apartamento;
import br.com.virtz.condominio.entidades.AreaComum;
import br.com.virtz.condominio.entidades.Bloco;
import br.com.virtz.condominio.entidades.BloqueioFuncaoUsuario;
import br.com.virtz.condominio.entidades.ParametroSistema;
import br.com.virtz.condominio.entidades.Reserva;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.service.ICondominioService;
import br.com.virtz.condominio.service.IReservaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class ReservaController {

	@EJB
	private ICondominioService condominioService;

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
	private Usuario usuarioLogado = null;
	private AreaComum areaSelecionada = null;
	private String mensagemConfirmacaoReserva = null ;
	private String mensagemErroReserva = null ;
	private ParametroSistema maximoDias = null;
	private ParametroSistema minimoDias = null;

	private boolean podeRemoverReserva;
	private boolean checkLiEConcordo;
	private Boolean check;
	private String mensagemExclusaoReserva = null;

	private List<Bloco> blocos = null;
	private List<AgrupamentoUnidades> agrupamentos = null;
	private Bloco blocoSelecionado;
	private Apartamento apartamentoSelecionado;
	private AgrupamentoUnidades agrupamentoSelecionado;


	@PostConstruct
	public void init(){
		reservas = new DefaultScheduleModel();
		usuarioLogado = sessao.getUsuarioLogado();

		montarListaAreasParaReserva();

		maximoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MAXIMO_PARA_AGENDAR_AREA_COMUM);
		minimoDias = parametroLookup.buscar(EnumParametroSistema.QUANTIDADE_DIAS_MINIMO_PARA_AGENDAR_AREA_COMUM);
		
		areaSelecionada = null;
		podeRemoverReserva = false;
		mensagemExclusaoReserva = null;

		// se for sindico pode agendar para outros moradores
		if(principalController.ehSindico() || principalController.ehAdministrativo()){
			carragarDadosSeForSindico();
		} else {
			blocoSelecionado = null;
			apartamentoSelecionado = null;
			agrupamentoSelecionado = null;
			usuarios = null;
		}
		if(areas != null && areas.size() == 1){
			areaSelecionada = areas.iterator().next();
			recuperarEventos();
		}

		if(principalController.condominioPossuiAgrupamento()){
			agrupamentos = condominioService.recuperarTodosAgrupamentos(usuarioLogado.getCondominio().getId());
		}

		checkLiEConcordo = false;
	}


	/**
	 * Carrega algumas variáveis necessárias se quem estiver agendando for um sindico.
	 * Permitindo agendar para outras pessoas.
	 */
	private void carragarDadosSeForSindico() {
		usuarios = usuarioService.recuperarTodos(usuarioLogado.getCondominio());

		blocoSelecionado = null;
		blocos = condominioService.recuperarTodosBlocosComApartamentos(usuarioLogado.getCondominio().getId());
		if(blocos != null && blocos.size() == 1){
			blocoSelecionado = blocos.get(0);
		}
		apartamentoSelecionado = null;
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

		if(getAreaSelecionada() != null){
			List<Reserva> reservasPersistidas = reservaService.recuperarRecentes(getAreaSelecionada());
			for(Reserva r : reservasPersistidas){
				Calendar hora = r.getData();
				hora.add(Calendar.HOUR_OF_DAY, 12);
				//hora.add(Calendar.DATE, 1); // gambiarra pra burlar o pau do componente q sempre exibe a data -1 dia.
				reservas.addEvent(new DefaultScheduleEvent(montarNomeEvento(r.getUsuario(), r.getApartamento()), hora.getTime(), hora.getTime()));
			}
		}
	}


	public void recuperarBlocosPorAgrupamento() {
		if(agrupamentoSelecionado != null){
			blocos = condominioService.recuperarTodosBlocosPorAgrupamento(agrupamentoSelecionado.getId());
			if(blocos != null && blocos.size() == 1){
				blocoSelecionado = blocos.get(0);
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
    	DataUtil dataUtil = new DataUtil();

    	Date dataSelecionada = (Date) selectEvent.getObject();

    	// validar se existe uma reserva para área nessa data.
    	for(ScheduleEvent event : reservas.getEvents()){
    		if(dataUtil.mesmoDiaMesAno(event.getStartDate(), dataSelecionada)){
    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    			mensagemErroReserva = "Já existe uma reserva cadastrada para o(a) "+getAreaSelecionada().getNome()+" no dia "+sdf.format(dataSelecionada)+".";
    			mensagemConfirmacaoReserva =null;
    			return;
    		}
    	}

    	Usuario usu = null;
    	Apartamento aptoAgendamento = apartamentoSelecionado;
    	if(aptoAgendamento == null){
    		usu = usuarioLogado;
    		aptoAgendamento = usu.getApartamento();
    	}

        evento = new DefaultScheduleEvent(montarNomeEvento(usu, aptoAgendamento), dataSelecionada, dataSelecionada);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String txtArea = " para o(a) "+getAreaSelecionada().getNome();
		mensagemConfirmacaoReserva = "Você confirma a reserva"+txtArea+" para o dia "+sdf.format(evento.getStartDate())+"?";
		mensagemErroReserva = null;
    }


	private String montarNomeEvento(Usuario usuario, Apartamento apartamento) {
		StringBuilder sb = new StringBuilder();

		if(apartamento != null){
			String nomeApto = usuarioLogado.getCondominio().getNomeUnidade();
			String nomeBloco = usuarioLogado.getCondominio().getNomeNivelAgrupamento1();
			String nomeAgrup = usuarioLogado.getCondominio().getNomeNivelAgrupamento2();
			if(usuarioLogado.getCondominio().condominioEhVertical()){
				//sb.append(nomeApto).append(": ").append(apartamento.getNumero());
				//sb.append(" ").append(nomeBloco).append(": ").append(apartamento.getBloco().getNome()).append("");
				sb.append(" [").append(principalController.getCondominio().getNomeUnidade()).append(" ").append(apartamento.getNumero()).append("]");
				sb.append("[").append(apartamento.getBloco().getNome()).append("]");
			}else{
				sb.append(" [").append(apartamento.getNumero()).append("][").append(apartamento.getBloco().getNome()).append("]");
				if(principalController.condominioPossuiAgrupamento()){
					sb.append(" [").append(apartamento.getBloco().getAgrupamentoUnidades().getNome()).append("]");
				}
			}
		}

		if(usuario != null){
			sb.append("  ").append(usuario.getNomeExibicao().toUpperCase()).append(" ");
		}

		return sb.toString();
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

        blocoSelecionado = null;
        apartamentoSelecionado = null;

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

		// caso tenha sido cadastrada alguma instrução para reserva o usuário tem q concordar com o termo.
		if(StringUtils.isNotBlank(this.areaSelecionada.getInstrucoesReserva()) && !this.isCheckLiEConcordo()){
			throw new AppException("Para efetuar a reservar você deve ler e concordar com o termo/instrução.");
		}

		// se data acima do limite deve rolar uma exceção
		Date dataMaxima = getDataMaximaAgendamento();
		if(dataMaxima != null && data.getTime().after(dataMaxima)){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			throw new AppException("A reserva não foi realizada. A data limite para agendamentos é "+sdf.format(dataMaxima)+". ");
		}
		
		// valida a proximidade da data da reserva. Se tiver configurado uma procedencia minima a reserva deve ser barrada.
		if(minimoDias != null || Integer.valueOf(minimoDias.getValor()) > 0){
			if(getDiferencaDiasEntreHojeEaReserva(data.getTime()) < Integer.valueOf(minimoDias.getValor())){
				throw new AppException("A reserva não foi realizada. A quantidade mínima de antecedência para reserva é de "+minimoDias.getValor()+" dias. ");
			}
		}

		Usuario usu = null;
		Apartamento aptoAgendamento = apartamentoSelecionado;
		if(aptoAgendamento == null){
			usu = usuarioLogado;
			aptoAgendamento = usu.getApartamento();
		}

		if(usu == null){
			List<Usuario> usuarios = usuarioService.recuperarUsuariosPorApartamento(aptoAgendamento.getId());
			if(usuarios != null && !usuarios.isEmpty()){
				usu = usuarios.get(0);
				for(Usuario u : usuarios){
					verificarUsuarioBloqueado(u);
				}
			}
		} else {
			verificarUsuarioBloqueado(usu);
		}

		Reserva reserva = new Reserva();
        reserva.setAreaComum(getAreaSelecionada());
        reserva.setData(data);
        reserva.setUsuario(usu);
        reserva.setApartamento(aptoAgendamento);

        try {
			reservaService.salvar(reserva);
		} catch (Exception e) {
			throw new AppException("Ocorreu um erro ao salvar a reserva.");
		}
        try {
        	if(usu != null){
        		enviarEmailConfirmacaoReserva(usu, reserva);
        	}
		} catch (Exception e) {

		}

	}


	public void verificarUsuarioBloqueado(Usuario usu) throws AppException {
		List<BloqueioFuncaoUsuario> bloqueioUsuario = usuarioService.recuperarBloqueios(usu.getId(), EnumFuncaoBloqueio.RESERVA);
		if(bloqueioUsuario != null && !bloqueioUsuario.isEmpty()){
			String comentario = bloqueioUsuario.get(0).getComentario();
			if(StringUtils.isBlank(comentario)){
				comentario = EnumFuncaoBloqueio.RESERVA.getMsgParaUsuario();
			}
			throw new AppException(comentario);
		}
	}

	public void removerReserva() throws AppException {
        if(evento != null) {
        	try{
	        	String apto = recuperarAptoDaReserva();
	        	String bloco = recuperarBlocoDaReserva();

	        	if(principalController.condominioPossuiAgrupamento()){
	        		String agrupamento = recuperarAgrupamentoDaReserva();
	        		reservaService.removerProAptoEData(getAreaSelecionada(), apto, bloco, agrupamento, evento.getStartDate());
	        	} else{
	        		reservaService.removerProAptoEData(getAreaSelecionada(), apto, bloco, evento.getStartDate());
	        	}
	        	reservas.deleteEvent(evento);
	        	message.addInfo("A reserva foi removida com sucesso!");
        	}catch(AppException e){
        		message.addError(e.getMessage());
        	}
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
	
	
	private Integer getDiferencaDiasEntreHojeEaReserva(Date dataReserva){
		DataUtil dt =new DataUtil();
		return dt.diasEntreDatas(dt.agora().getTime(), dt.limparHora(dataReserva));
	}


	public boolean verificarSePodeRemoverReserva() {
		if(usuarioLogado.isAdministrativo() || usuarioLogado.isSindico()){
			return Boolean.TRUE;
		}

		String apto = recuperarAptoDaReserva();
		String bloco = recuperarBlocoDaReserva();
		if(usuarioLogado.getApartamento().getNumero().equals(apto)
				&& usuarioLogado.getApartamento().getBloco().getNome().equals(bloco)){
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}


	private String recuperarEmailDaReserva() {
		String  emailReserva = evento.getTitle().substring(evento.getTitle().indexOf("["));
		emailReserva = emailReserva.substring(1, emailReserva.indexOf("]"));
		return emailReserva;
	}

	private String recuperarAptoDaReserva() {
		/*if(usuarioLogado.getCondominio().condominioEhVertical()){
			String[] arrayTitulo = evento.getTitle().split(" ");
			if(arrayTitulo == null && arrayTitulo.length <= 1){
				return null;
			}
			String  apto = arrayTitulo[1].trim();
			return apto;
		}else{
			String[] arrayTitulo = evento.getTitle().split(" \\| ");
			if(arrayTitulo == null && arrayTitulo.length <= 1){
				return null;
			}
			String  apto = arrayTitulo[0].trim();
			return apto;
		}*/

		String[] arrayTitulo = evento.getTitle().split("\\[");
		if(arrayTitulo == null && arrayTitulo.length < 2){
			return null;
		}

		String txt =arrayTitulo[1].replace(principalController.getCondominio().getNomeUnidade()+" ", "");

		String  apto = txt.substring(0,txt.indexOf("]")).trim();
		return apto;

	}

	private String recuperarBlocoDaReserva() {
		/*if(usuarioLogado.getCondominio().condominioEhVertical()){
			String[] arrayTitulo = evento.getTitle().split("Bloco:");
			if(arrayTitulo == null && arrayTitulo.length <= 1){
				return null;
			}
			String  padrao = arrayTitulo[1].trim();
			String  bloco = padrao;
			if(bloco.contains("[")){
				bloco = padrao.substring(0,padrao.indexOf("["));
			}
			return bloco.trim();
		}else{
			String[] arrayTitulo = evento.getTitle().split(" \\| ");
			if(arrayTitulo == null && arrayTitulo.length <= 1){
				return null;
			}
			String  bloco = arrayTitulo[1].trim();
			return bloco;
		}*/

		String[] arrayTitulo = evento.getTitle().split("\\[");
		if(arrayTitulo == null && arrayTitulo.length <= 2){
			return null;
		}
		String  bloco = arrayTitulo[2].substring(0,arrayTitulo[2].indexOf("]")).trim();
		return bloco;

	}

	private String recuperarAgrupamentoDaReserva() {
		/*String[] arrayTitulo = evento.getTitle().split(" \\| ");
		if(arrayTitulo == null && arrayTitulo.length <= 1){
			return null;
		}
		String[] novoArray = arrayTitulo[2].split("[");
		String  agrup = novoArray[0].trim();
		return agrup;*/

		String[] arrayTitulo = evento.getTitle().split("\\[");
		if(arrayTitulo == null && arrayTitulo.length <= 3){
			return null;
		}
		String  bloco = arrayTitulo[3].substring(0,arrayTitulo[3].indexOf("]")).trim();
		return bloco;


	}

	public boolean condominoPossuiAgrupamento(){
		return principalController.condominioPossuiAgrupamento();
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

	public String getMensagemErroReserva() {
		return mensagemErroReserva;
	}

	public Bloco getBlocoSelecionado() {
		return blocoSelecionado;
	}

	public void setBlocoSelecionado(Bloco blocoSelecionado) {
		this.blocoSelecionado = blocoSelecionado;
	}

	public Apartamento getApartamentoSelecionado() {
		return apartamentoSelecionado;
	}

	public void setApartamentoSelecionado(Apartamento apartamentoSelecionado) {
		this.apartamentoSelecionado = apartamentoSelecionado;
	}

	public List<Bloco> getBlocos() {
		return blocos;
	}

	public boolean isCheckLiEConcordo() {
		return checkLiEConcordo;
	}

	public void setCheckLiEConcordo(boolean checkLiEConcordo) {
		//this.checkLiEConcordo = checkLiEConcordo;
	}

	public AgrupamentoUnidades getAgrupamentoSelecionado() {
		return agrupamentoSelecionado;
	}

	public void setAgrupamentoSelecionado(AgrupamentoUnidades agrupamentoSelecionado) {
		this.agrupamentoSelecionado = agrupamentoSelecionado;
	}

	public List<AgrupamentoUnidades> getAgrupamentos() {
		return agrupamentos;
	}
	public void changeValueLiConcordo(){
		this.checkLiEConcordo = !this.checkLiEConcordo;
	}

}

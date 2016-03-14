package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.util.Base64;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.OpcaoVotacaoComImagem;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.entidades.Voto;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.geral.VotacaoView;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.service.IVotacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemVotacaoController {
	
	@EJB
	private IVotacaoService votacaoService;

	@EJB
	private IUsuarioService usuarioService;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private ParametroSistemaLookup parametroLookup; 
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private NavigationPage navigation;
	
	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@Inject
	private EnviarEmailSuporteController emailSuporte;
	
	
	
	private List<Votacao> votacoes;
	private List<Voto> votos = null;
	private Map<String, Integer> resultadoVotacaoSelecionada;
	private Usuario usuario = null;
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		votacoes = votacaoService.recuperarTodas(usuario.getCondominio());
		resultadoVotacaoSelecionada = new HashMap<String, Integer>();
	}
	
	public void ativarVotacao(Votacao votacao) throws CondominioException{
		try {
			votacaoService.ativarVotacao(votacao);
			if(votacoes != null){
				for(Votacao v: votacoes){
					if(v.equals(votacao)){
						v.setAtiva(Boolean.TRUE);
					}
				}
			}
		} catch (AppException e) {
			try{
				emailSuporte.enviarEmail("Ocorreu um erro ao ativar votação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A votação foi ativada!");
	}
	
	public void desativarVotacao(Votacao votacao) throws CondominioException{
		try {
			votacaoService.desativarVotacao(votacao);
			if(votacoes != null){
				for(Votacao v: votacoes){
					if(v.equals(votacao)){
						v.setAtiva(Boolean.FALSE);
					}
				}
			}
		} catch (AppException e) {
			try{
				emailSuporte.enviarEmail("Ocorreu um erro ao desativar votação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A votação foi desativada!");
	}
	
	public void excluirVotacao(Votacao votacao) throws CondominioException{
		try {
			votacaoService.removerVotacao(votacao);
		} catch (AppException e) {
			try{
				emailSuporte.enviarEmail("Ocorreu um erro ao excluir votação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
			throw new CondominioException(e.getMessage());
		}
		message.addInfo("A votação foi excluída!");
	}
	
	public void irParaCadastro(){
		navigation.redirectToPage("/votacao/cadastrarVotacao.faces");
	}
	
	public void editar(Votacao votacao){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idVotacao", votacao.getId());
		navigation.redirectToPage("/votacao/cadastrarVotacao.faces");
	}
	
	
	public boolean estaAtiva(Votacao votacao){
		if(votacao != null){
			return "ATIVA".equals(votacao.qualStatus());
		}
		return Boolean.FALSE;
	}
	
	
	public boolean estaInativa(Votacao votacao){
		if(votacao != null){
			return "INATIVA".equals(votacao.qualStatus());
		}
		return Boolean.TRUE;
	}
	
	
	public boolean estaEncerrada(Votacao votacao){
		if(votacao != null){
			return "ENCERRADA".equals(votacao.qualStatus());
		}
		return Boolean.FALSE;
	}
	
	
	public boolean possoRemover(Votacao votacao){
		if(votacao != null){
			if(!votacao.isAtiva()  && !estaEncerrada(votacao) && votacaoService.totalVotos(votacao) == 0) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	public boolean possoEditar(Votacao votacao){
		if(votacao != null){
			if(!estaEncerrada(votacao) && votacaoService.totalVotos(votacao) == 0) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	public void verResultado(Votacao votacao){
		if(votacao != null){
			try {
				resultadoVotacaoSelecionada = votacaoService.recuperarResultado(votacao.getId());
			} catch (AppException e) {
				resultadoVotacaoSelecionada = null;
				e.printStackTrace();
			}
		}
	}
	

	public void carregarVotos(Votacao votacaoSelecionada) {
		try {
			votos = votacaoService.recuperarTodosVotos(votacaoSelecionada.getId());
		} catch (AppException e) {
			e.printStackTrace();
			votos = null;
		}
	}
	
	public void encerrarVotacao(Votacao votacaoSelecionada) {
		try {
			votacaoService.encerrarVotacao(votacaoSelecionada.getId());
			votacoes = votacaoService.recuperarTodas(sessao.getUsuarioLogado().getCondominio());
			message.addInfo("A votação foi encerrada com sucesso! ");
			message.addInfo("Uma boa ideia seria enviar email avisando aos moradores. Para isso clique no icone 'Email' na coluna ações da votação.");
		} catch (AppException e) {
			try{
				emailSuporte.enviarEmail("Ocorreu um erro ao encerrar votação.", e.getMessage(), usuario.getCondominio().getId());
			}catch(Exception e1){
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.addError("Ocorreu um erro ao encerrar a votação");
		}
	}
	
	
	
	public void enviarEmailResultadoVotacao(Votacao votacao) {
		
		VotacaoView vv = null;
		
		// recupera os resultados
		if(votacao != null){
			vv = new VotacaoView(votacao);
			vv.getUtil().tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
			
			try {
				vv.setResultadoVotacaoSelecionada(votacaoService.recuperarResultado(votacao.getId()));
			} catch (AppException e) {
				try{
					emailSuporte.enviarEmail("Ocorreu um erro ao contar votos para envio de resultado.", e.getMessage(), usuario.getCondominio().getId());
				}catch(Exception e1){
				}
				e.printStackTrace();
				return;
			}
		} else{
			return;
		}
		
		// monta resultado para envio
		List<String> resultados = new ArrayList<String>();
		for(String chave : vv.getResultadoVotacaoSelecionada().keySet()){
			StringBuilder sb = new StringBuilder("[");
			sb.append(vv.getResultadoVotacaoSelecionada().get(chave)).append(" voto(s) - ").append(vv.getResultadoPercentagemVotacaoSelecionada().get(chave)).append("% ]   ");
			sb.append(chave).append("<br />");
			resultados.add(sb.toString());
		}
		
		List<String> anexos = null;
		if(EnumTipoVotacao.OPCOES_IMAGEM.equals(votacao.getTipoVotacao())){
			anexos = new ArrayList<String>();
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String url = origRequest.getRequestURL().substring(0, origRequest.getRequestURL().toString().indexOf("/votacao"));
			for(OpcaoVotacaoComImagem opc : votacao.getOpcoesComImagem()){
				StringBuilder sb = new StringBuilder("<div style='width:300px; text-align:center; height:300px; float:right;'>");
				
				sb.append("<label>").append(opc.getDescricao()).append("</label>");
				byte[] imgBytes = arquivoUtil.converter(opc.getImagemThumb().getNome());
				sb.append("<img src='data:image/png;base64,").append(Base64.encodeToString(imgBytes, false)).append("' />");
			
				
				sb.append("</div");
				anexos.add(sb.toString());
			}
		}
		
		
		// enviar emails
		Condominio c = sessao.getUsuarioLogado().getCondominio();
		List<Usuario> usuarios  = usuarioService.recuperarTodos(c);
		
		if(usuarios != null){
			for(Usuario u : usuarios){
				DataUtil dataUtil = new DataUtil();
				
				Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
				mapParametrosEmail.put("nome_usuario", u.getNomeExibicao());
				mapParametrosEmail.put("assunto_votacao", votacao.getAssuntoVotacao());
				mapParametrosEmail.put("resultados", resultados);
				mapParametrosEmail.put("anexos", anexos);
				
				String msg = leitor.processarTemplate( arquivoUtil.getCaminhaPastaTemplatesEmail(), EnumTemplates.RESULTADO_FINAL_VOTACAO.getNomeArquivo(), mapParametrosEmail);
				
				Email email = new Email(EnumTemplates.RESULTADO_FINAL_VOTACAO.getDe(), u.getEmail(), EnumTemplates.RESULTADO_FINAL_VOTACAO.getAssunto(), msg);
				enviarEmail.enviar(email);
			}
		}
		
		try {
			votacaoService.marcarEmailJaEnviado(votacao.getId());
		} catch (AppException e) {
			e.printStackTrace();
		}
		
		message.addInfo("Resultado enviado para todos os moradores do condomínio.");
	}
	
	
	/* GETTERS E SETTERS*/

	public List<Votacao> getVotacoes() {
		return votacoes;
	}

	public void setVotacoes(List<Votacao> votacoes) {
		this.votacoes = votacoes;
	}

	public Map<String, Integer> getResultadoVotacaoSelecionada() {
		return resultadoVotacaoSelecionada;
	}

	public void setResultadoVotacaoSelecionada(
			Map<String, Integer> resultadoVotacaoSelecionada) {
		this.resultadoVotacaoSelecionada = resultadoVotacaoSelecionada;
	}

	public List<Voto> getVotos() {
		return votos;
	}


}


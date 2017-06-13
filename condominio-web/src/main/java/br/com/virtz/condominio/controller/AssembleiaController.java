package br.com.virtz.condominio.controller;

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

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Token;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exception.ErroAoSalvar;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IPublicidadeService;
import br.com.virtz.condominio.service.ITokenService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class AssembleiaController {

	@EJB
	private IAssembleiaService assembleiaService;

	@EJB
	private IUsuarioService usuarioService;
	
	@EJB
	private IPublicidadeService publicidadeService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private IArquivosUtil arquivoUtil;

	@Inject
	private LeitorTemplate leitor;

	@EJB
	private EnviarEmail enviarEmail;

	@EJB
	private ITokenService tokenService;

	@Inject
	private EnviarEmailSuporteController emailSuporte;

	private List<Assembleia> assembleias;
	private Assembleia assembleiaSelecionada;
	private String textoPauta = null;

	private Usuario usuario = null;



	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		assembleias = listarTodos();
		leitor.setPublicidadeService(publicidadeService);
	}


	public List<Assembleia> listarTodos(){
		List<Assembleia> lista =  assembleiaService.recuperarAssembleiasNaoRealizadas(usuario.getCondominio().getId());
		return lista;
	}


	public void novaPauta() throws AppException{
		if(StringUtils.isNotBlank(textoPauta)){
			try {
				PautaAssembleia pauta = assembleiaService.novaPauta(assembleiaSelecionada.getId(), textoPauta, usuario);
				messageHelper.addInfo("Pauta enviada com sucesso! Aguarde até o sindico aprová-la.");

				try{
					enviarEmailPautaParaSindico(usuario, textoPauta, assembleiaSelecionada, pauta);
				}catch(Exception e){
					e.printStackTrace();
				}

				textoPauta = null;
				assembleiaSelecionada = null;

			} catch (ErroAoSalvar e) {
				try{
					emailSuporte.enviarEmail("Ocorreu um erro ao inserir nova pauta.", e.getMessage(), usuario.getCondominio().getId());
				}catch(Exception e1){
				}
				throw new AppException(e.getMessage());

			}
		}
	}


	private void enviarEmailPautaParaSindico(Usuario usuario, String txtPauta, Assembleia assembleia, PautaAssembleia pauta) {
		Token token = null;
		StringBuffer sb = null;
		try {
			token = tokenService.novoToken(pauta.getId().toString());
			// recuperar url da aplicação
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			sb = origRequest.getRequestURL().delete(origRequest.getRequestURL().indexOf("portal.faces"), origRequest.getRequestURL().toString().length());
			sb.append("assembleia/confirmarAprovacaoPauta.faces?token=").append(token.getToken());

		} catch (Exception e) {
			e.printStackTrace();
		}

		DataUtil dataUtil = new DataUtil();

		List<Usuario> sindicos = usuarioService.recuperarSindicos(usuario.getCondominio().getId());
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		if(sindicos != null){
			for(Usuario sindico : sindicos){
				Map<Object, Object> mapParametrosEmail = new HashMap<Object, Object>();
				mapParametrosEmail.put("nome_sindico", sindico.getNomeExibicao());
				mapParametrosEmail.put("data_assembleia", dataUtil.formatarData(assembleia.getData(),"dd/MM/yyyy"));
				mapParametrosEmail.put("nome_usuario", usuario.getNomeExibicao());
				mapParametrosEmail.put("msg", txtPauta);
				if(sb != null){
					mapParametrosEmail.put("link", sb.toString());
				} else {
					mapParametrosEmail.put("link", " - Nâo foi possível gerar o link - ");
				}

				String msg = leitor.processarTemplate(usuario.getCondominio().getId(), caminho, EnumTemplates.PAUTA_ENVIADA.getNomeArquivo(), mapParametrosEmail);

				Email email = new Email(EnumTemplates.PAUTA_ENVIADA.getDe(), sindico.getEmail(), EnumTemplates.PAUTA_ENVIADA.getAssunto(), msg);
				enviarEmail.enviar(email);
			}
		}
	}


	public void resetPauta(){
		textoPauta = null;
	}

	public boolean possuiAssembleiaAgendada(){
		if(assembleias == null || assembleias.isEmpty()){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}


	public void verPautas() {
        RequestContext.getCurrentInstance().openDialog("dialogVerPautas");
    }


	// GETTERS E SETTERS
	public List<Assembleia> getAssembleias() {
		return assembleias;
	}

	public void setAssembleias(List<Assembleia> assembleias) {
		this.assembleias = assembleias;
	}

	public Assembleia getAssembleiaSelecionada() {
		return assembleiaSelecionada;
	}

	public void setAssembleiaSelecionada(Assembleia assembleiaSelecionada) {
		this.assembleiaSelecionada = assembleiaSelecionada;
	}

	public String getTextoPauta() {
		return textoPauta;
	}

	public void setTextoPauta(String textoPauta) {
		this.textoPauta = textoPauta;
	}


}

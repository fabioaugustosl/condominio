package br.com.virtz.condominio.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.RegistroOcorrencia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IRegistroOcorrenciaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class RegistroOcorrenciaController {

	@EJB
	private IRegistroOcorrenciaService registroService;

	@Inject
	private MessageHelper messageHelper;

	@Inject
	private SessaoUsuario sessao;

	@Inject
	private LeitorTemplate leitor;

	@EJB
	private IUsuarioService usuarioService;

	@EJB
	private EnviarEmail enviarEmail;

	@Inject
	private ArquivosUtil arquivosUtil;

	@Inject
	private NavigationPage navegacao;



	private RegistroOcorrencia registroOcorrencia;
	private List<Usuario> usuarios = null;
	private Usuario usuarioSessao = null;
	private Usuario usuario;


	@PostConstruct
	public void init(){
		registroOcorrencia = new RegistroOcorrencia();
		usuarioSessao = sessao.getUsuarioLogado();
		usuario = null;
		usuarios = usuarioService.recuperarTodos(usuarioSessao.getCondominio());
	}


	public void salvarOcorrencia(ActionEvent event) throws CondominioException {
		try{
			DataUtil dataUtil = new DataUtil();
			if(registroOcorrencia.getDataOcorrencia() != null && !dataUtil.dataEhMenorQueHoje(registroOcorrencia.getDataOcorrencia())){
				messageHelper.addError("A data do ocorrido não pode ser maior que hoje.");
				return;
			}

			if(registroOcorrencia.getDataOcorrencia() == null){
				registroOcorrencia.setDataOcorrencia(new Date());
			}

			registroOcorrencia.setCondominio(usuario.getCondominio());
			if(this.usuario != null){
				registroOcorrencia.setUsuario(usuario);
			} else {
				registroOcorrencia.setUsuario(usuarioSessao);
			}
			registroOcorrencia = registroService.salvar(registroOcorrencia);
			messageHelper.addInfo("Sua ocorrência foi registrada com sucesso.");

			try {
				List<Usuario> sindicos = usuarioService.recuperarSindicos(usuario.getCondominio().getId());
				envioEmail(sindicos, registroOcorrencia);
			}catch(Exception e1){
				messageHelper.addError("Sua mensagem foi salva porém ocorreu um erro ao enviar o email para o sindíco.");
			}

			usuario = null;
			registroOcorrencia = new RegistroOcorrencia();
		}catch(Exception e){
			e.printStackTrace();
			messageHelper.addError("Ocorreu um erro inesperado ao salvar nova ocorrência.");
		}
	}

	public List<Usuario> autoCompleteUsuarios(String query){
		List<Usuario> auto = new ArrayList<Usuario>();

		if(StringUtils.isBlank(query)){
			return usuarios;
		}

		for(Usuario u : usuarios){
			if(u.getNomeExibicao().contains(query) || u.getApartamento().getNumero().contains(query)){
				auto.add(u);
			}
		}

		return auto;
	}

	public void irParaListagem(){
		navegacao.redirectToPage("/ocorrencia/listagemRegistroOcorrencia.faces");
	}

	private void envioEmail(List<Usuario> sindicos, RegistroOcorrencia registroOcorrencia) {
		for(Usuario sindico : sindicos){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("nome_sindico", sindico.getNomeExibicao());
			map.put("nome_usuario", usuario.getNomeExibicao());
			map.put("msg", registroOcorrencia.getMensagem());

			String caminho = arquivosUtil.getCaminhaPastaTemplatesEmail();
			String msgEnviar = leitor.processarTemplate(caminho, EnumTemplates.REGISTRO_OCORRENCIA.getNomeArquivo(), map);

			Email email = new Email(EnumTemplates.REGISTRO_OCORRENCIA.getDe(), sindico.getEmail(), EnumTemplates.REGISTRO_OCORRENCIA.getAssunto(), msgEnviar);
			enviarEmail.enviar(email);
		}

	}



	// GETTERS E SETTERS

	public RegistroOcorrencia getRegistroOcorrencia() {
		return registroOcorrencia;
	}

	public void setRegistroOcorrencia(RegistroOcorrencia registroOcorrencia) {
		this.registroOcorrencia = registroOcorrencia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}


}

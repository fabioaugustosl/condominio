package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoNotificacaoPortaria;
import br.com.virtz.condominio.entidades.NotificacaoPortaria;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.INotificacaoPortariaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class NotificacaoPortariaController {

	@EJB
	private INotificacaoPortariaService notificacaoService;

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
	private NavigationPage navegacao;

	@Inject
	private IArquivosUtil arquivoUtil;



	private NotificacaoPortaria notificacaoPortaria;
	private Usuario usuarioSessao = null;
	private ArquivoNotificacaoPortaria arquivoNotificacao = null;


	@PostConstruct
	public void init(){
		notificacaoPortaria = new NotificacaoPortaria();
		usuarioSessao = sessao.getUsuarioLogado();
	}


	public void salvar(ActionEvent event) throws CondominioException {
		try{
			DataUtil dataUtil = new DataUtil();
			if(notificacaoPortaria.getDataPrevista() != null && dataUtil.dataEhMenorQueHoje(notificacaoPortaria.getDataPrevista())){
				messageHelper.addError("A data prevista não pode ser menor que hoje.");
			}

			if(arquivoNotificacao != null){
				notificacaoPortaria.setArquivo(arquivoNotificacao);
			}

			notificacaoPortaria.setCondominio(usuarioSessao.getCondominio());
			notificacaoPortaria.setUsuario(usuarioSessao);

			notificacaoService.salvar(notificacaoPortaria);
			messageHelper.addInfo("Sua notificação foi registrada com sucesso.");

			/*try {
				List<Usuario> sindicos = usuarioService.recuperarSindicos(usuario.getCondominio().getId());
				envioEmail(sindicos, registroOcorrencia);
			}catch(Exception e1){
				messageHelper.addError("Sua mensagem foi salva porém ocorreu um erro ao enviar o email para o sindíco.");
			}*/

			notificacaoPortaria = new NotificacaoPortaria();
			arquivoNotificacao = null;
		}catch(Exception e){
			e.printStackTrace();
			messageHelper.addError("Ocorreu um erro inesperado ao salvar a notificação.");
		}
	}



	/*public void irParaListagem(){
		navegacao.redirectToPage("/ocorrencia/listagemRegistroOcorrencia.faces");
	}
*/

	/*private void envioEmail(List<Usuario> sindicos, RegistroOcorrencia registroOcorrencia) {
		for(Usuario sindico : sindicos){
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("nome_sindico", sindico.getNomeExibicao());
			map.put("nome_usuario", usuario.getNomeExibicao());
			map.put("msg", registroOcorrencia.getMensagem());

			String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
			String msgEnviar = leitor.processarTemplate(caminho, EnumTemplates.REGISTRO_OCORRENCIA.getNomeArquivo(), map);

			Email email = new Email(EnumTemplates.REGISTRO_OCORRENCIA.getDe(), sindico.getEmail(), EnumTemplates.REGISTRO_OCORRENCIA.getAssunto(), msgEnviar);
			enviarEmail.enviar(email);
		}

	}*/


	public void uploadArquivo(FileUploadEvent arquivo) throws CondominioException {
        try {
        	InputStream inputStream = arquivo.getFile().getInputstream();

			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = arquivo.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_DOCUMENTO);

			arquivoNotificacao = new ArquivoNotificacaoPortaria();
			arquivoNotificacao.setCaminho(caminho);
			arquivoNotificacao.setExtensao(extensao);
			arquivoNotificacao.setNomeOriginal(nomeAntigo);
			arquivoNotificacao.setTamanho(arquivo.getFile().getSize());
			arquivoNotificacao.setNome(nomeNovo);

			if(arquivoUtil.ehImagem(arquivoNotificacao.getExtensao())){
				arquivoUtil.redimensionarImagem(inputStream, arquivoUtil.getCaminhoArquivosUpload(), nomeNovo, extensao, 1000, 1000);
			} else {
				arquivoUtil.arquivar(inputStream, nomeNovo);
			}

			messageHelper.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
        	messageHelper.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
    }


	public void removerArquivo() throws CondominioException {
		if(arquivoNotificacao != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivoNotificacao.getNome());
			arquivoDeletar.delete();

			arquivoNotificacao = null;

			messageHelper.addInfo("Arquivo removido com sucesso!");
		}
	}



	/*public String getCaminhoCompletoThumb(String nomeArq){
		String ext = "."+arquivoUtil.pegarExtensao(nomeArq);
		String novoNome = nomeArq.replace(ext, "");
		return novoNome+ArquivosUtil.THUMB_POS_FIXO+ext;
	}*/


	// GETTERS E SETTERS

	public NotificacaoPortaria getNotificacaoPortaria() {
		return notificacaoPortaria;
	}

	public void setNotificacaoPortaria(NotificacaoPortaria notificacaoPortaria) {
		this.notificacaoPortaria = notificacaoPortaria;
	}

	public ArquivoNotificacaoPortaria getArquivoNotificacao() {
		return arquivoNotificacao;
	}

	public void setArquivoNotificacao(ArquivoNotificacaoPortaria arquivoNotificacao) {
		this.arquivoNotificacao = arquivoNotificacao;
	}


}

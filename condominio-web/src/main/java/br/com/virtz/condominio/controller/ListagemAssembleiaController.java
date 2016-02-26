package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.bean.Email;
import br.com.virtz.condominio.constantes.EnumTemplates;
import br.com.virtz.condominio.constantes.EnumTipoUsuario;
import br.com.virtz.condominio.email.EnviarEmail;
import br.com.virtz.condominio.email.template.LeitorTemplate;
import br.com.virtz.condominio.entidades.ArquivoAtaAssembleia;
import br.com.virtz.condominio.entidades.Assembleia;
import br.com.virtz.condominio.entidades.MensagemSindico;
import br.com.virtz.condominio.entidades.PautaAssembleia;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IAssembleiaService;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class ListagemAssembleiaController {

	@EJB
	private IAssembleiaService assembleiaService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	@Inject
	private LeitorTemplate leitor;
	
	@EJB
	private EnviarEmail enviarEmail;
	
	@EJB
	private IUsuarioService usuarioService;

	private DataUtil dataUtil;
	
	
	private List<Assembleia> assembleias;
	private Assembleia assembleiaSelecionada;
	private String mensagemLembrete;
	
	
	@PostConstruct
	public void init(){
		mensagemLembrete = null;
		assembleias = listarTodos(); 
		dataUtil = new DataUtil();
	}
	
	
	public List<Assembleia> listarTodos(){
		Usuario usuario = sessao.getUsuarioLogado();
		
		List<Assembleia> lista =  assembleiaService.recuperarTodos(usuario.getCondominio().getId());
		
		return lista;
	}
	
	
	@SuppressWarnings("static-access")
	public void irParaCadastro(){
		navegacao.redirectToPage("/assembleia/cadastrarAssembleia.faces");
	}
	
	
	 public StreamedContent download(ArquivoAtaAssembleia arquivo) throws CondominioException {        
		 if(arquivo != null){
			InputStream stream;
			try {
				stream = new FileInputStream(new File(arquivoUtil.getCaminhoArquivosUpload()+arquivo.getNome()));
				StreamedContent file = new DefaultStreamedContent(stream, arquivoUtil.getMimetypeArquivo(arquivo.getExtensao()), arquivo.getNomeOriginal());
				return file;
			} catch (FileNotFoundException e) {
				messageHelper.addError("O arquivo para download não foi encontrado.");
				e.printStackTrace();
			}
		 }
		 return null;
	 }
	 
	
	 public void removerPauta(PautaAssembleia pauta) throws CondominioException {
		 assembleiaSelecionada.getPautas().remove(pauta);
		 assembleiaService.removerPauta(pauta.getId());
		 messageHelper.addInfo("Pauta removida com sucesso!");
		 
	 }
	 
	 
	 public void removerAssembleia(Assembleia assembleia) throws CondominioException {

		 String nomeArquivo = null;
		 if(assembleia.getArquivoAta() != null){
			 nomeArquivo = assembleia.getArquivoAta().getNome();
		 }
		 assembleiaService.removerAssembleia(assembleia.getId());
		 assembleias.remove(assembleia);
		 messageHelper.addInfo("Assembleia removida com sucesso!");

		 try {
			 if(StringUtils.isNotBlank(nomeArquivo)){
				 File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+nomeArquivo);
				 arquivoDeletar.delete();
			 }
		 }catch(Exception e){
			 // foda-se
		 }
	 }
	 
	 
	 public void editar(Assembleia assembleia){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idAssembleia", assembleia.getId());
		irParaCadastro();
	 }	
	 
	 
	 public boolean podeEditar(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	 }
	 
	 public boolean podeExcluir(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	 }

	 public boolean podeInserir(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	 }
	 
	 public boolean ehSindico(){
		 if(EnumTipoUsuario.SINDICO.equals(sessao.getUsuarioLogado().getTipoUsuario())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	 }
	 
	 public boolean naoAconteceu(Assembleia assembleia){
		 if(dataUtil.agora().getTime().before(assembleia.getData())){
			 return Boolean.TRUE;
		 }
		 return Boolean.FALSE;
	 }
	 
	 
	 private void envioEmail(List<Usuario> moradores, String msg,  Assembleia assembleia) {
		String caminho = arquivoUtil.getCaminhaPastaTemplatesEmail();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
		for(Usuario morador : moradores){
			
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("nome_usuario", morador.getNome());
			map.put("msg", msg);
			map.put("data", sdf.format(assembleia.getData()));
			map.put("chamada_1", sdfHora.format(assembleia.getHorario1()));
			if(assembleia.getHorario2() != null){
				map.put("chamada_2", sdfHora.format(assembleia.getHorario2()));
			}
			
			String msgEnviar = leitor.processarTemplate(caminho, EnumTemplates.LEMBRETE_ASSEMBLEIA.getNomeArquivo(), map);
			
			Email email = new Email(EnumTemplates.LEMBRETE_ASSEMBLEIA.getDe(), morador.getEmail(), EnumTemplates.LEMBRETE_ASSEMBLEIA.getAssunto(), msgEnviar);
			enviarEmail.enviar(email);
		}
		
	 }

	 
	 public void enviarLembrete() throws CondominioException {

		 if(StringUtils.isNotBlank(mensagemLembrete)){
			 if(assembleiaSelecionada == null){
				 messageHelper.addError("Ocorreu um erro ao identificar dados da Assembleia. ");
			 } else {
			 
				 List<Usuario> usuarios = usuarioService.recuperarTodos(sessao.getUsuarioLogado().getCondominio());
				 envioEmail(usuarios, mensagemLembrete, assembleiaSelecionada);
			 
				 assembleiaSelecionada = null;
				 mensagemLembrete = null;
			 	 messageHelper.addInfo("Lembrete enviado com sucesso!");
			 }
		 } else {
			 messageHelper.addError("Lembrete não enviado. A mensagem é obrigatória.");
		 }
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

	 public String getMensagemLembrete() {
		return mensagemLembrete;
	 }

	 public void setMensagemLembrete(String mensagemLembrete) {
		this.mensagemLembrete = mensagemLembrete;
	 }
	 	 
		
}

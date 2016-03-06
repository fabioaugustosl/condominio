package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;

import br.com.virtz.condominio.constantes.EnumTipoVotacao;
import br.com.virtz.condominio.entidades.ArquivoOpcaoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacao;
import br.com.virtz.condominio.entidades.OpcaoVotacaoComImagem;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.entidades.Votacao;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.geral.ParametroSistemaLookup;
import br.com.virtz.condominio.geral.UtilTipoVotacao;
import br.com.virtz.condominio.service.IVotacaoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class CadastroVotacaoController {
	
	@EJB
	private IVotacaoService votacaoService;
	
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
	private UploadArquivoController uploadController;
	
	
		
	private Votacao votacao;
	private List<String> tiposVotacao;
	private String tipoVotacaoSelecionado;
	private UtilTipoVotacao utilTipoVotacao;
	private String descricaoNovaOpcao;
	private ArquivoOpcaoVotacao arqOpcao;
	private ArquivoOpcaoVotacao arqOpcaoThumb;
	
	
	@PostConstruct
	public void init(){
		Usuario usuario = sessao.getUsuarioLogado();
		montarComboTipoVotacao();
		utilTipoVotacao = new UtilTipoVotacao();
		Object votacaoEditar = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idVotacao");
		if(votacaoEditar == null){
			votacao = votacaoService.criarNovaVotacao(usuario, usuario.getCondominio(), null, null);
			tipoVotacaoSelecionado = null;
		} else {
			votacao = votacaoService.buscar(Long.parseLong(votacaoEditar.toString()));
			tipoVotacaoSelecionado = votacao.getTipoVotacao().getDescricao();
			utilTipoVotacao.tratarTipoVotacaoExibicao(votacao.getTipoVotacao());
		}
		arqOpcao = null;
		arqOpcaoThumb = null;
	}

	
	private void montarComboTipoVotacao(){
		tiposVotacao = new ArrayList<String>();
		for(EnumTipoVotacao tipoVotacao : EnumTipoVotacao.values()){
			tiposVotacao.add(tipoVotacao.getDescricao());
		}
	}

	
	public void montarOpcaoTipoVotacao(){
		if(StringUtils.isNotBlank(tipoVotacaoSelecionado)){
			EnumTipoVotacao tipoVotacao = EnumTipoVotacao.recuperarPorDescricao(tipoVotacaoSelecionado);
			utilTipoVotacao.tratarTipoVotacaoExibicao(tipoVotacao);
		}
	}
	
	
	public void adicionarOpcao(ActionEvent event){
		if(StringUtils.isNotBlank(descricaoNovaOpcao)){
			OpcaoVotacao opcao = votacao.adicionarNovaOpcao(descricaoNovaOpcao);
		}
		descricaoNovaOpcao = null;
	}

	
	public void removerOpcao(OpcaoVotacao opcaoRemover){
		votacaoService.removerOpcao(opcaoRemover);
		votacao.removerOpcao(opcaoRemover);
	}
	
	
	public void adicionarOpcaoImagem(ActionEvent event){
		
		if(votacao.getOpcoesComImagem()!= null && votacao.getOpcoesComImagem().size() >= 9){
			message.addError("É permitido no máximo 9 opções por votação.");
			return;
		}
		
		if(arqOpcao == null){
			message.addError("Quando você seleciona opções cadastrar com imagem a mesma é obrigatória.");
			return;
		}
		
		if(StringUtils.isNotBlank(descricaoNovaOpcao)){
			OpcaoVotacaoComImagem opcao = votacao.adicionarNovaOpcaoComImagem(descricaoNovaOpcao, arqOpcao, arqOpcaoThumb);
		}
		descricaoNovaOpcao = null;
		arqOpcao = null;
		arqOpcaoThumb = null;
	}

	
	public void removerOpcaoImagem(OpcaoVotacaoComImagem opcaoRemover){

		votacaoService.removerOpcaoImagem(opcaoRemover);
		votacao.removerOpcaoComImagem(opcaoRemover);
		
		try {
			removerArquivo(opcaoRemover.getImagem());
		} catch (CondominioException e) {
			e.printStackTrace();
		}
	}
	
	
	public void salvarVotacao(ActionEvent event) throws CondominioException {
		if(votacao == null){
			throw new CondominioException("Nenhuma votação encontrada para ser salva.");
		}
		
		votacao.setTipoVotacao(EnumTipoVotacao.recuperarPorDescricao(getTipoVotacaoSelecionado()));
		
		if(this.isOpcoes() && (votacao.getOpcoes()== null || votacao.getOpcoes().isEmpty()) && votacao.getOpcoes().size() < 2){
			throw new CondominioException("Você selecionou o tipo de votação por opções, porém devem ser cadastradas no mínimo duas (2) opções.");
		}
		
		if(this.isOpcoesComImagem() && (votacao.getOpcoesComImagem()== null || votacao.getOpcoesComImagem().isEmpty()) && votacao.getOpcoesComImagem().size() < 2){
			throw new CondominioException("Você selecionou o tipo de votação por opções, porém devem ser cadastradas no mínimo duas (2) opções.");
		}
		
		if(votacao.getDataLimite() != null && votacao.getDataLimite().before(new Date())){
			message.addError("A data limite da votação deve ser maior que a data atual.");
			return;
		}
		
		try{
			votacaoService.salvarVotacao(votacao);
			message.addInfo("A votação foi salva com sucesso.");
			
			votacao = votacaoService.criarNovaVotacao(sessao.getUsuarioLogado(), sessao.getUsuarioLogado().getCondominio(), null, null);
			utilTipoVotacao.passarTodosParaFalso();
			tipoVotacaoSelecionado = null;
		}catch(Exception e){
			e.printStackTrace();
			message.addError("Ocorreu um erro inesperado ao salvar a nova votação. Favor tente novamente.");
		}
	}
	
	
	public void uploadArquivo(FileUploadEvent evento) throws CondominioException {
        try {
        	InputStream inputStream = evento.getFile().getInputstream();
			
			if(!arquivoUtil.tamanhoImagemEhValido(inputStream, 100, 100)){
        		message.addError("A imagem é muito pequena! Ela deve ter largura mínima de 100 e altura mínima de 100 pixels.");
        		return;
        	}

			String nomeNovo = uploadController.uploadArquivo(evento, ArquivosUtil.TIPO_IMAGEM);
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = evento.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			
			arqOpcao = new ArquivoOpcaoVotacao();
			arqOpcao.setCaminho(caminho);
			arqOpcao.setExtensao(extensao);
			arqOpcao.setNomeOriginal(nomeAntigo);
			arqOpcao.setTamanho(evento.getFile().getSize());
			arqOpcao.setNome(nomeNovo);
			
			gerarThumbImagemOpcao(evento, caminho, nomeAntigo, extensao, nomeNovo);
			
			message.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
            message.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
    }


	private void gerarThumbImagemOpcao(FileUploadEvent event, String caminho,
			String nomeAntigo, String extensao, String nomeNovo) {
		try{
			// cria um thumb
			arquivoUtil.gravarThumb(nomeNovo);
			
			arqOpcaoThumb = new ArquivoOpcaoVotacao();
			arqOpcaoThumb.setCaminho(caminho);
			arqOpcaoThumb.setExtensao(extensao);
			arqOpcaoThumb.setNomeOriginal(nomeAntigo);
			arqOpcaoThumb.setTamanho(event.getFile().getSize());
			arqOpcaoThumb.setNome(arquivoUtil.getCaminhoCompletoThumb(nomeNovo));
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	
	public void removerArquivo(ArquivoOpcaoVotacao arquivo) throws CondominioException {
		if(arquivo != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivo.getNome());
			arquivoDeletar.delete();
			
			try{
				File arquivoDeletarThumb = new File(arquivoUtil.getCaminhoCompletoThumb(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arquivo.getNome()));
				arquivoDeletarThumb.delete();
			}catch(Exception e1){
			}
			arqOpcaoThumb = null;
			arqOpcao = null;
			message.addInfo("Arquivo removido com sucesso!");
		}
	}
	
	public void irParaListagem(){
		navigation.redirectToPage("/votacao/listagemVotacao.faces");
	}
	
	/* GETTERS E SETTERS*/
		
	public Votacao getVotacao() {
		return votacao;
	}
	
	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
	}

	public List<String> getTiposVotacao() {
		return tiposVotacao;
	}

	public String getTipoVotacaoSelecionado() {
		return tipoVotacaoSelecionado;
	}

	public void setTipoVotacaoSelecionado(String tipoVotacaoSelecionado) {
		this.tipoVotacaoSelecionado = tipoVotacaoSelecionado;
	}

	public boolean isSimNao() {
		return utilTipoVotacao.isSimNao();
	}

	public boolean isData() {
		return utilTipoVotacao.isData();
	}

	public boolean isMoeda() {
		return utilTipoVotacao.isMoeda();
	}

	public boolean isNumerico() {
		return utilTipoVotacao.isNumerico();
	}

	public boolean isOpcoes() {
		return utilTipoVotacao.isOpcoes();
	}
	
	public boolean isOpcoesComImagem() {
		return utilTipoVotacao.isOpcoesImagem();
	}

	public UtilTipoVotacao getUtilTipoVotacao() {
		return utilTipoVotacao;
	}

	public String getDescricaoNovaOpcao() {
		return descricaoNovaOpcao;
	}

	public void setDescricaoNovaOpcao(String descricaoNovaOpcao) {
		this.descricaoNovaOpcao = descricaoNovaOpcao;
	}

	public ArquivoOpcaoVotacao getArqOpcao() {
		return arqOpcao;
	}

	public void setArqOpcao(ArquivoOpcaoVotacao arqOpcao) {
		this.arqOpcao = arqOpcao;
	}
	
	
}


package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import br.com.virtz.boleto.util.DataUtil;
import br.com.virtz.condominio.constantes.EnumTipoBalanco;
import br.com.virtz.condominio.entidades.ArquivoBalanco;
import br.com.virtz.condominio.entidades.Balanco;
import br.com.virtz.condominio.entidades.ItemBalanco;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exception.AppException;
import br.com.virtz.condominio.exceptions.CondominioException;
import br.com.virtz.condominio.service.IBalancoService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.ArquivosUtil;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;

@ManagedBean
@ViewScoped
public class LancarBalancoController {

	
	@EJB
	private IBalancoService balancoService;
	
	@Inject
	private MessageHelper message;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	private UploadedFile arquivo;
	
	private Usuario usuario = null;
	
	private Integer ano = null;
	private Integer mes = null;
	private List<Integer> anos = null;
	private Map<Integer, String> meses = null;
	
	private Balanco balanco = null;
	private ItemBalanco itemEditar = null;
	private ArquivoBalanco arqBalanco = null;
	private String descricao = null;
	private Double valor = null;
	private String tipoBalanco = null;
	
	private List<ItemBalanco> receitas = null;
	private List<ItemBalanco> despesas = null;
	private List<String> descricoesAutoCompletar = null;

	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		receitas = new ArrayList<ItemBalanco>();
		despesas = new ArrayList<ItemBalanco>();
		tipoBalanco = EnumTipoBalanco.RECEITA.name().toString();
		
		/*valorBase = usuario.getCondominio().getValorCondominioMes();
		if(valorBase == null){
			valorBase = 0d;
			message.addWarn("O valor do condomínio está zerado! Para configurar o valor base do condomínio acesse o menu Configurações > Meu Condominio ");
		}*/
		carregarInformacoesPeriodoPadroes();
		montarBalanco();
		carregarDescricoesAutoCompletar(EnumTipoBalanco.RECEITA);
	}

	public void carregarDescricoesAutoCompletar(EnumTipoBalanco tipo) {
		if(tipo == null){
			if(StringUtils.isNotBlank(tipoBalanco)){
				tipo = EnumTipoBalanco.valueOf(tipoBalanco);
			} else{
				tipo = EnumTipoBalanco.DESPESA;
			}
		}
		try {
			descricoesAutoCompletar = balancoService.recuperarUltimasDescricoes(usuario.getId(), this.ano, tipo);
		} catch (AppException e) {
		}
	}
	
	public void limparDadosItem(){
		arqBalanco = null;
		descricao = null;
		valor = null;
		itemEditar = null;
	}

	private void carregarInformacoesPeriodoPadroes() {
		DataUtil dataUtil = new DataUtil();
		meses = dataUtil.listarMesesSelecao();
		anos = dataUtil.listarAnosSelecao(null);
		this.ano = dataUtil.agora().get(Calendar.YEAR);
		this.mes = dataUtil.agora().get(Calendar.MONTH)+1;
	}

	private void criarBalanco() {
		
		balanco = new Balanco();
		balanco.setCondominio(usuario.getCondominio());
		balanco.setMes(mes);
		balanco.setAno(ano);
		
	}
	
	public void montarBalanco(){
		
		if(this.mes == null){
			return;
		}
		if(this.ano == null){
			return;
		}
		
		balanco = balancoService.recuperarBalancoPorCondominio(usuario.getCondominio().getId(), this.ano, this.mes);
		
		if(balanco == null){
			criarBalanco();
			try {
				balanco = balancoService.salvar(balanco);
			} catch (AppException e) {
				message.addError("Ocorreu um erro grave ao inicializar o lançamento do balanço");
			}
		} 
		
		try {
			despesas = balancoService.recuperarDespesas(balanco);
			receitas = balancoService.recuperarReceitas(balanco);
		} catch (AppException e) {
		}

		balanco.zerarTotais();
		
		atualizarSomatorio();
		
		limparDadosItem();
	}

	private void atualizarSomatorio() {
		try {
			balanco.setTotalReceitas(balancoService.somarItens(receitas));
			balanco.setTotalDespesas(balancoService.somarItens(despesas));
		} catch (AppException e) {
		}
	}


	public List<String> completeText(String query) {
        List<String> resultados = new ArrayList<String>();
        if(descricoesAutoCompletar != null && !descricoesAutoCompletar.isEmpty()){
        	for(String d : descricoesAutoCompletar){
        		if(d.toUpperCase().contains(query.toUpperCase())){
        			resultados.add(d);
        		}
        	}
        }
        Collections.sort(resultados);
        return resultados;
    }
	
	
	public void salvarItemBanlanco(ActionEvent event) throws CondominioException {
		try{
			
			EnumTipoBalanco tipo = EnumTipoBalanco.valueOf(tipoBalanco);
			if(itemEditar != null){
				balancoService.salvarItem(itemEditar);
			}else {
				
				if(EnumTipoBalanco.DESPESA.equals(tipo)){
					ItemBalanco i = balancoService.salvarDespesa(balanco.getId(), valor, descricao, arqBalanco, usuario);
					despesas.add(i);
				}else{
					ItemBalanco i = balancoService.salvarReceita(balanco.getId(), valor, descricao, arqBalanco, usuario);
					receitas.add(i);
				}
			}
			message.addInfo("A "+tipo.getDescricao()+" foi salva com sucesso.");
			
			atualizarSomatorio();
			
			limparDadosItem();
			
		}catch(AppException appE){
			message.addError(appE.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao salvar o lançamento do título.");
		}
	}
	
	
	public void removerItemBanlanco(ItemBalanco item) throws CondominioException {
		try{
			String desc = item.getTipoBalanco().getDescricao();
			if(EnumTipoBalanco.DESPESA.equals(item.getTipoBalanco())){
				despesas.remove(item);
			} else {
				receitas.remove(item);
			}
			
			String nomeArquivo = null;
			 if(item.getArquivo() != null){
				 nomeArquivo = item.getArquivo().getNome();
			 }
			 
			 balancoService.removerItemBalanco(item.getId());
			 atualizarSomatorio();
			 
			 if(nomeArquivo != null){
				 File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+nomeArquivo);
				 arquivoDeletar.delete();
			 }
			
			message.addInfo(desc+" foi removido com sucesso.");
		}catch(AppException appE){
			message.addError(appE.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			throw new CondominioException("Ocorreu um erro inesperado ao remover item de balanço");
		}
	}
	
	
	public void editarItemBanlanco(ItemBalanco item) throws CondominioException {
			
		arqBalanco = item.getArquivo();
		descricao = item.getDescricao();
		valor = item.getValor();
		tipoBalanco = item.getTipoBalanco().name().toString();
		
	}


	public void uploadArquivo(FileUploadEvent event) {
        try {
			InputStream inputStream = event.getFile().getInputstream();
			
			String caminho = arquivoUtil.getCaminhoArquivosUpload();
			String nomeAntigo = event.getFile().getFileName();
			String extensao = arquivoUtil.pegarExtensao(nomeAntigo);
			String nomeNovo = arquivoUtil.gerarNomeArquivo(extensao, ArquivosUtil.TIPO_ARQUIVO_BALANCO);
			
			arqBalanco = new ArquivoBalanco();
			arqBalanco.setCaminho(caminho);
			arqBalanco.setExtensao(extensao);
			arqBalanco.setNomeOriginal(nomeAntigo);
			arqBalanco.setTamanho(event.getFile().getSize());
			arqBalanco.setNome(nomeNovo);
			
			arquivoUtil.arquivar(inputStream, nomeNovo);
			
			message.addInfo("Arquivo "+nomeAntigo+" foi anexado com sucesso.");
        } catch (IOException e) {
            message.addError("Ocorreu um erro ao fazer upload do arquivo. Favor acessar novamente na tela.");
        }
    }
	
	
	public void removerArquivo(ActionEvent event) throws CondominioException {
		if(arquivo != null){
			File arquivoDeletar = new File(arquivoUtil.getCaminhoArquivosUpload()+"\\"+arqBalanco.getNome());
			arquivoDeletar.delete();
			arquivo = null;
			arqBalanco = null;
			message.addInfo("Arquivo removido com sucesso!");
		}
	}
	
	
	/* GETTER E SETTERS*/
	
	

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public List<Integer> getAnos() {
		return anos;
	}

	public Map<Integer, String> getMeses() {
		return meses;
	}

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getTipoBalanco() {
		return tipoBalanco;
	}

	public void setTipoBalanco(String tipoBalanco) {
		this.tipoBalanco = tipoBalanco;
	}

	public List<ItemBalanco> getReceitas() {
		return receitas;
	}

	public void setReceitas(List<ItemBalanco> receitas) {
		this.receitas = receitas;
	}

	public List<ItemBalanco> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<ItemBalanco> despesas) {
		this.despesas = despesas;
	}

	public Balanco getBalanco() {
		return balanco;
	}

	public ArquivoBalanco getArqBalanco() {
		return arqBalanco;
	}
	
	
}

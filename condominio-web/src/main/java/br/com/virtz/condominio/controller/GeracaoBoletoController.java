package br.com.virtz.condominio.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.jrimum.bopepo.Boleto;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.virtz.boleto.ConversorBoletoPdf;
import br.com.virtz.boleto.GeradorBoleto;
import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoEndereco;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;
import br.com.virtz.condominio.entidades.CobrancaBoleto;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.geral.DataUtil;
import br.com.virtz.condominio.service.IUsuarioService;
import br.com.virtz.condominio.session.SessaoUsuario;
import br.com.virtz.condominio.util.IArquivosUtil;
import br.com.virtz.condominio.util.MessageHelper;
import br.com.virtz.condominio.util.NavigationPage;

@ManagedBean
@ViewScoped
public class GeracaoBoletoController {

	@EJB
	private IUsuarioService usuarioService;
	
	@Inject
	private MessageHelper messageHelper;
	
	@Inject
	private SessaoUsuario sessao;
	
	@Inject
	private NavigationPage navegacao;
	
	@Inject
	private IArquivosUtil arquivoUtil;
	
	
	private Usuario usuario = null;
	
	private CobrancaBoleto cobranca = null;
	
	private List<Usuario> usuarios = null;
	
	
	@PostConstruct
	public void init(){
		usuario = sessao.getUsuarioLogado();
		usuarios = listarTodos(); 
		cobranca = new CobrancaBoleto();
		cobranca.setValorBase(250d);
		DataUtil dataUtil = new DataUtil();
		Calendar agora = dataUtil.agora();
		cobranca.setMesReferencia(agora.get(Calendar.MONTH)+1);
		cobranca.setAnoReferencia(agora.get(Calendar.YEAR));
	}
	

	public List<Usuario> listarTodos(){
		List<Usuario> usuarios = usuarioService.recuperarTodos(usuario.getCondominio());
		return usuarios;
	}
	
	
	public StreamedContent download(Usuario usuario) {   
		
		InfoCedente cedente = new InfoCedente();
		cedente.setNome(usuario.getCondominio().getNome());
		cedente.setCnpj("00.000.208/0001-00");
		
		InfoSacado sacado = new InfoSacado();
		sacado.setCpf("014.483.675-06");
		sacado.setNome(usuario.getNome());
		
		InfoEndereco end = new InfoEndereco();
		end.setBairro("Castelo");
		end.setCep("35600-00");
		end.setCidade("Belo Horizonte");
		end.setLogradouro("Rua Paschoal Costa");
		end.setNumero("180");
		end.setSiglaEstado("MG");
		sacado.setEndereco(end);
		
		
		Conta conta = new Conta();
		conta.setCodigoBanco("001");
		conta.setCodigoCarteira(123456);
		//conta.setDigitoVerificadorAgencia(digitoVerificadorAgencia);
		conta.setNumeroAgencia("0012");
		conta.setDigitoVerificadorAgencia("6");
		conta.setNumeroConta("00123456");
		conta.setDigitoVerificadorAgencia("7");
		
		InfoTitulo tit = new InfoTitulo();
		tit.setDataDocumento(new Date());
		tit.setDataVencimento(new Date());
		tit.setInstrucoesSacado("Instruções sacado");
		tit.setNossoNumero("12345678901234567");
		tit.setValor(cobranca.getValorBase());
		tit.setInstrucaoLinha1(cobranca.getInstrucaoLinha1());
		tit.setInstrucaoLinha2(cobranca.getInstrucaoLinha2());
		tit.setInstrucoes("Pagamento referente a "+cobranca.getMesReferencia()+"/"+cobranca.getAnoReferencia());
		tit.setNumeroDocumento("1010");
		tit.setDescricaoLocalPagamento("Pagar no caixa eletronico");

		GeradorBoleto gerador = new GeradorBoleto();
		ConversorBoletoPdf conversor = new ConversorBoletoPdf();
		
		Boleto boleto = gerador.gerar(cedente, conta, sacado, tit);
		File arqBoleto = conversor.converter(boleto, "ExemploBoletoBB");
		
		if(arqBoleto != null){
			InputStream stream;
			try {
				stream = new FileInputStream(arqBoleto);
				StreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "ExemploBoletoBB.pdf");
				return file;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		 }
		 return null;
    }


	
	
	
	
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public CobrancaBoleto getCobranca() {
		return cobranca;
	}

	public void setCobranca(CobrancaBoleto cobranca) {
		this.cobranca = cobranca;
	}
	
		
}

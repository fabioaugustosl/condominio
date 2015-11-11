package br.com.virtz.boleto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jrimum.bopepo.Boleto;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.EnumBanco;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;
import br.com.virtz.boleto.formatador.FabricaFormatadorDadosBancarios;
import br.com.virtz.boleto.formatador.FormatadorDadosBancarios;
import br.com.virtz.boleto.util.FormatadorTexto;


public class GeradorBoleto {

	private FormatadorDadosBancarios formatador = null;
	
	
	public Boleto gerar(InfoCedente infoCedente, Conta conta, InfoSacado infoSacado, InfoTitulo infoTitulo){
		formatador = FabricaFormatadorDadosBancarios.recuperarFormatador(EnumBanco.recuperarPorCodigo(conta.getCodigoBanco()));
		Cedente cedente = criarCedente(infoCedente);
		Sacado sacado = criarSacado(infoSacado);
		ContaBancaria contaBancaria = criarContaBancaria(conta);
		
		Titulo t = new Titulo(contaBancaria, sacado, cedente, new SacadorAvalista(""));
		congigurarTitulo(t, infoTitulo);
		
		Boleto boleto = new Boleto(t);
		
		configurarTextosBoleto(infoTitulo, boleto);
		
		return boleto;
	}
	

	private void configurarTextosBoleto(InfoTitulo infoTitulo, Boleto boleto) {
		boleto.setInstrucaoAoSacado(infoTitulo.getInstrucoesSacado());

		if(infoTitulo.getInstrucoes() !=null && !infoTitulo.getInstrucoes().isEmpty()){
			FormatadorTexto formata = new FormatadorTexto();
			
			List<String> linhas = formata.quebrarStrings(infoTitulo.getInstrucoes(), 100);
	
			if(linhas != null && !linhas.isEmpty()){
				
				if(linhas.size() > 1 && StringUtils.isNotBlank(linhas.get(0))){
					boleto.setInstrucao1(linhas.get(0));
				}
				if(linhas.size() > 2 && StringUtils.isNotBlank(linhas.get(1))){
					boleto.setInstrucao2(linhas.get(1));
				}
				if(linhas.size() > 3 && StringUtils.isNotBlank(linhas.get(2))){
					boleto.setInstrucao3(linhas.get(2));
				}
				if(linhas.size() > 4 && StringUtils.isNotBlank(linhas.get(3))){
					boleto.setInstrucao4(linhas.get(3));
				}
				if(linhas.size() > 5 && StringUtils.isNotBlank(linhas.get(4))){
					boleto.setInstrucao5(linhas.get(4));
				}
				if(linhas.size() > 6 && StringUtils.isNotBlank(linhas.get(5))){
					boleto.setInstrucao6(linhas.get(5));
				}
				if(linhas.size() > 7 && StringUtils.isNotBlank(linhas.get(6))){
					boleto.setInstrucao7(linhas.get(6));
				}
				if(linhas.size() > 8 && StringUtils.isNotBlank(linhas.get(7))){
					boleto.setInstrucao8(linhas.get(7));
				}
			}
		}
		boleto.setLocalPagamento(infoTitulo.getDescricaoLocalPagamento());
	}
	
	
	private void congigurarTitulo(Titulo titulo, InfoTitulo infoTitulo){
        titulo.setNumeroDoDocumento(infoTitulo.getNumeroDocumento());
        titulo.setNossoNumero(infoTitulo.getNossoNumero());
        titulo.setDigitoDoNossoNumero(infoTitulo.getDigitoNossoNumero());
        titulo.setValor(BigDecimal.valueOf(infoTitulo.getValor()));
        titulo.setDataDoDocumento(infoTitulo.getDataDocumento());
        titulo.setDataDoVencimento(infoTitulo.getDataVencimento());
        titulo.setTipoDeDocumento(infoTitulo.getTipoTitulo());
        titulo.setAceite(infoTitulo.getAceiteConvertido());
        titulo.setDesconto(new BigDecimal(infoTitulo.getDesconto()));
        titulo.setDeducao(new BigDecimal(infoTitulo.getDeducao()));
        titulo.setMora(new BigDecimal(infoTitulo.getMora()));
        titulo.setAcrecimo(new BigDecimal(infoTitulo.getAcrescimo()));
        titulo.setValorCobrado(new BigDecimal(infoTitulo.getValorCobrado()));
   	}
	
	
	private ContaBancaria criarContaBancaria(Conta conta){
		ContaBancaria contaBancariaRetorno = new ContaBancaria(conta.getBanco().create());
		if(StringUtils.isNotBlank(conta.getDigitoVerificadorConta())){
			contaBancariaRetorno.setNumeroDaConta(new NumeroDaConta(Integer.valueOf(formatador.formatarConta(conta.getNumeroConta())), formatador.formatarDigitoConta(conta.getDigitoVerificadorConta())));
		} else {
			contaBancariaRetorno.setNumeroDaConta(new NumeroDaConta(Integer.valueOf(formatador.formatarConta(conta.getNumeroConta()))));
		}
		if(conta.getCodigoCarteira() != null){
			contaBancariaRetorno.setCarteira(new Carteira(conta.getCodigoCarteira()));
		}
		if(StringUtils.isNotBlank(conta.getDigitoVerificadorAgencia())){
			contaBancariaRetorno.setAgencia(new Agencia(Integer.valueOf(conta.getNumeroAgencia()), conta.getDigitoVerificadorAgencia()));
        } else {
        	contaBancariaRetorno.setAgencia(new Agencia(Integer.valueOf(conta.getNumeroAgencia())));
        }
        
        return contaBancariaRetorno;
	}
	
	
	private Cedente criarCedente(InfoCedente cedente){
		Cedente cedenteRetorno = null;
		if(StringUtils.isBlank(cedente.getCnpjFormatado())){		
			cedenteRetorno = new Cedente(cedente.getNome());
		} else {
			cedenteRetorno = new Cedente(cedente.getNome(), cedente.getCnpjFormatado());
		}
		
		return cedenteRetorno;
	}
	
	
	private Sacado criarSacado(InfoSacado sacado){
		Sacado sacadoRetorno = null;
		if(StringUtils.isNotBlank(sacado.getCpfFormatado())){
			sacadoRetorno = new Sacado(sacado.getNome(), sacado.getCpfFormatado());
		} else {
			sacadoRetorno = new Sacado(sacado.getNome());
		}
		
		Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(sacado.getEndereco().getUnidadeFederativa());
        enderecoSac.setLocalidade(sacado.getEndereco().getCidade());
        
        if(StringUtils.isNotBlank(sacado.getCpfFormatado())){
        	enderecoSac.setCep(new CEP(sacado.getEndereco().getCepFormatado()));
        }
        
        enderecoSac.setBairro(sacado.getEndereco().getBairro());
        enderecoSac.setLogradouro(sacado.getEndereco().getLogradouro());
        enderecoSac.setNumero(sacado.getEndereco().getNumero());
        sacadoRetorno.addEndereco(enderecoSac);
         
		return sacadoRetorno;
	}
	
	
}

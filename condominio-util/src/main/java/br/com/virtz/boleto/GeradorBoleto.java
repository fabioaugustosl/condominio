package br.com.virtz.boleto;

import java.math.BigDecimal;
import java.util.Date;

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
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;


public class GeradorBoleto {

	public Boleto gerar(InfoCedente infoCedente, Conta conta, InfoSacado infoSacado, InfoTitulo infoTitulo){
		
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
		boleto.setInstrucao1(infoTitulo.getInstrucaoLinha1());
		boleto.setInstrucao2(infoTitulo.getInstrucaoLinha2());
		boleto.setInstrucao3(infoTitulo.getInstrucaoLinha3());
		boleto.setInstrucao6(infoTitulo.getInstrucoes());
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
			contaBancariaRetorno.setNumeroDaConta(new NumeroDaConta(Integer.valueOf(conta.getNumeroConta()), conta.getDigitoVerificadorConta()));
		} else {
			contaBancariaRetorno.setNumeroDaConta(new NumeroDaConta(Integer.valueOf(conta.getNumeroConta())));
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

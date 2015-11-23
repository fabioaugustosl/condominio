package br.com.virtz.condominio.boleto.conversor;

import java.text.ParseException;
import java.util.Date;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;

import br.com.virtz.boleto.bean.Conta;
import br.com.virtz.boleto.bean.InfoCedente;
import br.com.virtz.boleto.bean.InfoEndereco;
import br.com.virtz.boleto.bean.InfoSacado;
import br.com.virtz.boleto.bean.InfoTitulo;
import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.Condominio;
import br.com.virtz.condominio.entidades.ContaBancariaCondominio;
import br.com.virtz.condominio.entidades.Usuario;
import br.com.virtz.condominio.exceptions.ErroConversaoException;

public class ConversorDadosBoleto {

	public InfoCedente criarCedente(Condominio condominio) throws ErroConversaoException{ 
		
		InfoCedente cedente = new InfoCedente();
		if(condominio != null && StringUtils.isNotBlank(condominio.getNome())){
			cedente.setNome(condominio.getNome());
		}
		if(condominio != null && StringUtils.isNotBlank(condominio.getCnpj())){
			try {
				MaskFormatter mascaraCnpj = new MaskFormatter("###.###.###/####-##");
				String cnpj =  condominio.getCnpj().replaceAll("[^0-9]*", "");  
				cnpj = mascaraCnpj.valueToString(cnpj);
				cedente.setCnpj(cnpj);
			} catch (ParseException e) {
			}
		}
		return cedente;
	}
	
	public InfoSacado criarSacado(Usuario usuario) throws ErroConversaoException{ 
		InfoSacado sacado = new InfoSacado();
		if(usuario != null){
			if(StringUtils.isNotBlank(usuario.getNome())){
				sacado.setNome(usuario.getNome());
			}

			if(StringUtils.isNotBlank(usuario.getCpf())){
				try {
					MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
					String cpf =  usuario.getCpf().replaceAll("[^0-9]*", "");  
					cpf = mascaraCpf.valueToString(cpf);
					sacado.setCpf(cpf);
				} catch (ParseException e) {
				}
			}
		}
		return sacado;
	}
		
	public InfoEndereco criarEndereco(Condominio condominio) throws ErroConversaoException{
		InfoEndereco end = new InfoEndereco();
		end.setBairro(condominio.getBairro());
		
		MaskFormatter mascaraCep;
		if(StringUtils.isNotBlank(condominio.getCep())){
			try {
				mascaraCep = new MaskFormatter("#####-###");
				String cep =  condominio.getCep().replaceAll("[^0-9]*", "");  
				cep = mascaraCep.valueToString(cep);
				end.setCep(cep);
			} catch (ParseException e) {
			}
		}
		
		if(condominio.getCidade()!= null){
			end.setCidade(condominio.getCidade().getNome());
			try {
				if(condominio.getCidade().getEstado()!= null){
					end.setSiglaEstado(condominio.getCidade().getEstado().getUf());
				}
			}catch(Exception e){}
		}
		
		end.setLogradouro(condominio.getEndereco());
		if(condominio.getNumero() != null){
			end.setNumero(condominio.getNumero().toString());
		}
		return end;
	}
	
	public Conta criarConta(ContaBancariaCondominio contaCond) throws ErroConversaoException{
		if(contaCond.getNumeroConta() == null){
			throw new ErroConversaoException("O número da conta é obrigatório.");
		}
		Conta conta = new Conta();
		conta.setCodigoBanco(contaCond.getBanco().getCodigo());
		conta.setCodigoCarteira(Integer.parseInt(contaCond.getCodigoCarteira()));
		conta.setNumeroAgencia(contaCond.getAgencia());
		conta.setDigitoVerificadorAgencia(contaCond.getDigitoAgencia());
		conta.setNumeroConta(contaCond.getNumeroConta());
		if(StringUtils.isNotBlank(contaCond.getDigitoConta())){
			conta.setDigitoVerificadorConta(contaCond.getDigitoConta());
		} 
		return conta;
	}

	/**
	 * Tem que passar a cobrança com a configuração carregado
	 * @param cobranca
	 * @return
	 */
	public InfoTitulo criarTitulo(CobrancaUsuario cobranca) throws ErroConversaoException{
		
		if(cobranca.getConfiguracaoBoleto() == null){
			throw new ErroConversaoException("Não foi possível recuperar a configuração do título.");
		}
		InfoTitulo tit = new InfoTitulo();
		tit.setDataDocumento(new Date());
		tit.setDataVencimento(cobranca.getConfiguracaoBoleto().getDataVencimento());
		tit.setInstrucoesSacado(cobranca.getConfiguracaoBoleto().getInstrucaoSacado());
		tit.setNossoNumero(cobranca.getNossoNumero());
		tit.setDigitoNossoNumero(cobranca.getDigitoNossoNumero());
		tit.setValor(cobranca.getValor());
		tit.setInstrucoes(cobranca.getConfiguracaoBoleto().getInstrucoesGerais());
		tit.setNumeroDocumento(cobranca.getId().toString());
		tit.setDescricaoLocalPagamento(cobranca.getConfiguracaoBoleto().getInstrucaoLocalPgto());
		return tit;
	}
}

package br.com.virtz.condominio.service;

import java.util.List;

import javax.ejb.Local;

import br.com.virtz.condominio.entidades.CobrancaUsuario;
import br.com.virtz.condominio.entidades.ConfiguracaoBoleto;
import br.com.virtz.condominio.entidades.Taxa;
import br.com.virtz.condominio.exception.AppException;

@Local
public interface IFinanceiroService {
	
	// Taxas
	public Taxa salvar(Taxa taxa) throws AppException;
	public List<Taxa> recuperarTaxaPorCondominio(Long idCondominio);
	public Taxa recuperarTaxa(Long idTaxa);
	public void removerTaxa(Long idTaxa) throws AppException;

	
	// Configuração Boleto
	public ConfiguracaoBoleto salvar(ConfiguracaoBoleto configuracao) throws AppException;
	public List<ConfiguracaoBoleto> recuperarConfiguracaoBoleto(Long idConfiguracaoBoleto);
	public ConfiguracaoBoleto recuperarConfiguracaoPorCondominio(Long idCondominio, Integer ano, Integer mes);
	
	
	// Cobranças 
	public CobrancaUsuario salvar(CobrancaUsuario cobranca) throws AppException;
	public List<CobrancaUsuario> recuperarCobrancas(Long idCondominio, Integer ano, Integer mes);
	public CobrancaUsuario recuperarCobranca(Long idCondominio, Long idUsuario, Integer ano, Integer mes);
	public List<CobrancaUsuario> recuperarCobrancasDeAvulsos(Long idCondominio, Integer ano, Integer mes);
	public List<CobrancaUsuario> recuperarCobrancasUsuario(Long idCondominio, Long idUsuario);
	public List<CobrancaUsuario> gerarCobrancas(ConfiguracaoBoleto configuracao) throws AppException;
	public List<String> recuperarAnosMesesDispiniveis(Long idCondominio);
	public CobrancaUsuario atualizarCobranca(Long idCobranca, String nossoNumero, String digitoNossoNumero, String codigoBarras) throws AppException;
	
	
}
